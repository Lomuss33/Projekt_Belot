import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
enum SSEType { WRITE, CALL, SCRIPT, LOAD, CLEAR, RELEASE; }
class LiveView {
    final HttpServer server;
    final int port;
    static int defaultPort = 50_001;
    static final String index = "./web/index.html";
    static Map<Integer,LiveView> views = new ConcurrentHashMap<>();
    List<String> paths = new ArrayList<>();

    static void setDefaultPort(int port) { defaultPort = port != 0 ? Math.abs(port) : 50_001; }
    static int getDefaultPort() { return defaultPort; }

    List<HttpExchange> sseClientConnections;

    // lock required to temporarily block processing of `SSEType.LOAD`
    Lock lock = new ReentrantLock();
    Condition loadEventOccurredCondition = lock.newCondition();
    boolean loadEventOccured = false;


    static LiveView onPort(int port) {
        port = Math.abs(port);
        try {
            if (!views.containsKey(port))
                views.put(port, new LiveView(port));
            return views.get(port);
        } catch (IOException e) {
            System.err.printf("Error starting Server: %s\n", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    static LiveView onPort() { return onPort(defaultPort); }

    private LiveView(int port) throws IOException {
        this.port = port;
        sseClientConnections = new CopyOnWriteArrayList<>(); // thread-safe variant of ArrayList

        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        System.out.println("Open http://localhost:" + port + " in your browser");

        // loaded-Request to signal successful processing of SSEType.LOAD
        server.createContext("/loaded", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
            lock.lock();
            try { // try/finally pattern for locks
                loadEventOccured = true;
                loadEventOccurredCondition.signalAll();
            } finally {
                lock.unlock();
            }
        });

        // SSE context
        server.createContext("/events", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }
            exchange.getResponseHeaders().add("Content-Type", "text/event-stream");
            exchange.getResponseHeaders().add("Cache-Control", "no-cache");
            exchange.getResponseHeaders().add("Connection", "keep-alive");
            exchange.sendResponseHeaders(200, 0);
            sseClientConnections.add(exchange);
        });

        // initial html site
        server.createContext("/", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }
            final String path = exchange.getRequestURI().getPath().equals("/") ? index
                    : "." + exchange.getRequestURI().getPath();
            try (final InputStream stream = new FileInputStream(path)) {
                final byte[] bytes = stream.readAllBytes();
                exchange.getResponseHeaders().add("Content-Type", Files.probeContentType(Path.of(path)) + "; charset=utf-8");
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
                exchange.getResponseBody().flush();
            } finally {
                exchange.close();
            }
        });

        server.setExecutor(Executors.newFixedThreadPool(5));
        server.start();
    }

    void sendServerEvent(SSEType sseType, String data) {
        List<HttpExchange> deadConnections = new ArrayList<>();
        for (HttpExchange connection : sseClientConnections) {
            if (sseType == SSEType.LOAD) lock.lock();
            try {
                byte[] binaryData = data.getBytes(StandardCharsets.UTF_8);
                String base64Data = Base64.getEncoder().encodeToString(binaryData);
                String message = "data: " + sseType + ":" + base64Data + "\n\n";
                connection.getResponseBody()
                          .write(message.getBytes());
                connection.getResponseBody().flush();
                if (sseType == SSEType.LOAD && !loadEventOccured) {
                    loadEventOccurredCondition.await(1_000, TimeUnit.MILLISECONDS);
                    if (loadEventOccured) paths.add(data);
                    else System.err.println("LOAD-Timeout: " + data);
                }
            } catch (IOException e) {
                deadConnections.add(connection);
            } catch (InterruptedException e) {
                System.err.println("LOAD-Timeout: " + data + ", " + e);
            } finally {
                if (sseType == SSEType.LOAD) {
                    loadEventOccured = false;
                    lock.unlock();
                }
            }
        }
        sseClientConnections.removeAll(deadConnections);
    }

    void createResponseContext(String path, Consumer<String> delegate) {
        createResponseContext(path, delegate, "-1");
    }

    void createResponseContext(String path, Consumer<String> delegate, String id) {
        server.createContext(path, exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }

            String content_length = exchange.getRequestHeaders().getFirst("Content-length");
            if (content_length == null) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }

            try {
                int length = Integer.parseInt(content_length);
                byte[] data = new byte[length];
                exchange.getRequestBody().read(data);
                delegate.accept(new String(data));
                sendServerEvent(SSEType.RELEASE, id);
            } catch (NumberFormatException e) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }

            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        });
    }

    public void stop() {
        sseClientConnections.clear();
        views.remove(port);
        server.stop(0);
    }

    static void shutdown() {
        views.forEach((k, v) -> v.stop());
    }
}
interface Clerk {
    static String generateID(int n) { // random alphanumeric string of size n
        return new Random().ints(n, 0, 36).
                            mapToObj(i -> Integer.toString(i, 36)).
                            collect(Collectors.joining());
    }

    static String getHashID(Object o) { return Integer.toHexString(o.hashCode()); }

    static LiveView view(int port) { return LiveView.onPort(port); }
    static LiveView view() { return view(LiveView.getDefaultPort()); }

    static void write(LiveView view, String html)        { view.sendServerEvent(SSEType.WRITE, html); }
    static void call(LiveView view, String javascript)   { view.sendServerEvent(SSEType.CALL, javascript); }
    static void script(LiveView view, String javascript) { view.sendServerEvent(SSEType.SCRIPT, javascript); }
    static void load(LiveView view, String path) {
        if (!view.paths.contains(path.trim())) view.sendServerEvent(SSEType.LOAD, path);
    }
    static void load(LiveView view, String onlinePath, String offlinePath) {
        load(view, onlinePath + ", " + offlinePath);
    }
    static void clear(LiveView view) { view.sendServerEvent(SSEType.CLEAR, ""); }
    static void clear() { clear(view()); };

    static void markdown(String text) { new MarkdownIt(view()).write(text); }
}
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
class Text { // Class with static methods for file operations
    static void write(String fileName, String text) {
        try {
            Files.writeString(Path.of(fileName), text);
        } catch (IOException e) {
            System.err.printf("Error writing %s\n", e.getMessage());
            System.exit(1);
        }
    }

    // core method
    static String cutOut(Path path, boolean includeStartLabel, boolean includeEndLabel, String... labels) {
        List<String> snippet = new ArrayList<>();
        boolean skipLines = true;
        boolean isInLabels;
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                isInLabels = Arrays.stream(labels).anyMatch(label -> line.trim().equals(label));
                if (isInLabels) {
                    if (skipLines && includeStartLabel)
                        snippet.add(line);
                    if (!skipLines && includeEndLabel)
                        snippet.add(line);
                    skipLines = !skipLines;
                    continue;
                }
                if (skipLines)
                    continue;
                snippet.add(line);
            }
        } catch (IOException e) {
            System.err.printf("Error reading %s\n", e.getMessage());
            System.exit(1);
        }
        return snippet.stream().collect(Collectors.joining("\n"));
    }
    // end

    static String cutOut(Path path, String... labels) { return cutOut(path, false, false, labels); }
    static String read(Path path) { return cutOut(path, true, true, ""); }

    static String cutOut(String fileName, boolean includeStartLabel, boolean includeEndLabel, String... labels) {
        return cutOut(Path.of(fileName), includeStartLabel, includeEndLabel, labels);
    }
    static String cutOut(String fileName, String... labels) {
        return cutOut(fileName, false, false, labels);
    }
    static String read(String fileName) {
        return cutOut(fileName, true, true, "");
    }

    static String escapeHtml(String text) {
        return text.replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;");
    }

    // Method `fillOut` emulates String interpolation, since String Templates
    // have been removed in Java 23 (they were a preview feature in Java 21 and 22).

    static String fillOut(Map<String, Object> replacements, String template) {
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}"); // `${<key>}`
        Matcher matcher = pattern.matcher(template);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            if (!replacements.containsKey(key))
                System.err.println("WARNING: key \"" + key + "\" not found in template:\n" + template);
            Object replacement = replacements.getOrDefault(key, "${" + key + "}"); 
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement.toString()));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    static String fillOut(String template, Map<String, Object> replacements) {
        return fillOut(replacements, template);
    }

    static String fillOut(String template, Object... replacements) {
        Map<String, Object> m = new HashMap<>();
        IntStream.range(0, replacements.length)
            .forEach(i -> m.put(Integer.toString(i), replacements[i]));
        return fillOut(m, template);
    }
}
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
abstract class ObjectNode_425 {
    String name;
    Optional<String> value;
    String identifier;
    boolean isDotted;
    ObjectNode_425[] children;
    /**
     * @param name - custom name to uniquely identify node in dot graph 
     * @param value - values of primitive types/strings or classname; displayed inside the dot node
     * @param identifier - variable name; displayed on dot arrow
     * @param isDotted - dot node with dotted lines
     * @param children - child nodes
     */
    ObjectNode_425(String name, Optional<String> value, String identifier, boolean isDotted, ObjectNode_425... children) {
        this.name = name;
        this.value = value;
        this.identifier = identifier;
        this.children = children;
        this.isDotted = isDotted;
    }

    @Override
    public String toString() {
        String output =  "";
        if (children != null && children.length > 0) {
            for (ObjectNode_425 child : children) {
                if (child == null) continue;
                output += child.toString();
                output += this.name +  " -> " + child.name + "[label=\" "+ child.identifier + "\",style=" + (child.isDotted ? "dashed" : "solid") +"] ;\n";
            }
        }
        return output;
    }
}
class RootNode_425 extends ObjectNode_425 {
    /**
     * root / start of the graph; start point is pointing on this node.
     * @param name - custom name to uniquely identify node in dot graph 
     * @param value - values of primitive types/strings or classname; displayed inside the dot node
     * @param identifier - variable name; displayed on dot arrow
     * @param children - child nodes
     */
    RootNode_425(String name, Optional<String> value, String identifier, ObjectNode_425... children) {
        super(name, value, identifier, false, children);
    }

    @Override
    public String toString() {
        String output = "start[shape=circle,label=\"\",height=.25];\n";
        output += this.name + (value.isPresent() ? " [label=\""+ this.value.get() + "\"];\n" : " [label=\"\",shape=point,height=.25];\n");
        output += "start -> " + name + "[label=\" "+ identifier + "\"] ;\n";

        return output + super.toString();
    }
}
class ChildNode_425 extends ObjectNode_425 {
    /**
     * child nodes
     * @param name - custom name to uniquely identify node in dot graph 
     * @param value - values of primitive types/strings or classname; displayed inside the dot node
     * @param identifier - variable name; displayed on dot arrow
     * @param isDotted - dot node with dotted lines
     * @param children - child nodes
     */
    ChildNode_425(String name, Optional<String> value, String identifier, boolean isDotted, ObjectNode_425... children) {
        super(name, value, identifier, isDotted, children);
    }

    @Override
    public String toString() {
        String output = this.name + (value.isPresent() ? " [label=\""+ this.value.get() + "\",style=" + (isDotted ? "dashed" : "solid") +"];\n" : " [label=\"\",shape=point,height=.25];\n");

        return output + super.toString();
    }
}
class ArrayNode extends ObjectNode_425 {
    int length;
    /**
     * array nodes are displayed as a box and have an additional value for the array length
     * can aswell be used to display any collection or map
     * @param name - custom name to uniquely identify node in dot graph 
     * @param value - values of primitive types/strings or classname; displayed inside the dot node
     * @param identifier - variable name; displayed on dot arrow
     * @param length - array length
     * @param isDotted - dot node with dotted lines
     * @param children - child nodes
     */
    ArrayNode(String name, Optional<String> value, String identifier, int length, boolean isDotted, ObjectNode_425... children) {
        super(name, value, identifier, isDotted, children);
        this.length = length;
    }

    @Override
    public String toString() {
        String output = this.name + (value.isPresent() ? " [label=\""+ this.value.get() + "\",shape=box,style=" + (isDotted ? "dashed" : "solid") +"];\n" : " [label=\"\",shape=point,height=.25];\n");
        output += this.name + "length[label=\""+ this.length + "\"];\n";
        output += this.name + "->" + this.name + "length[label=\"length\"]\n";
        return output + super.toString();
    }
}
class NodeGenerator {
    private int nodeCounter = 0; //used to generate an unique node name
        
    // save inspected objects to prevent infinite loops in case of recursion and identify already used objects 
    private Map<Object, ObjectNode_425> inspectedObject = new HashMap<>();

    private ObjectNode_425 root;

    private boolean hideGeneratedVars, inspectSuperClasses;

    private NodeGenerator(){}

    /**
     * Inspect the object using reflections and store it in a tree structure of Nodes
     * @param objectToBeInspected - root object of the tree structure; 
     * @param identifier - variable name referencing the object 
     * @return instance of NodeGenerator
     */
    public static NodeGenerator inspect(Object objectToBeInspected, String identifier) {
        return inspect(objectToBeInspected, identifier, true, true);
    }

     /**
     * Inspect the object using reflections and store it in a tree structure of Nodes
     * @param objectToBeInspected - root object of the tree structure; 
     * @param identifier - variable name referencing the object 
     * @param inspectSuperClasses - true -> super class fields are inspected too
     * @return instance of NodeGenerator
     */
    public static NodeGenerator inspect(Object objectToBeInspected, String identifier, boolean inspectSuperClasses) {
        return inspect(objectToBeInspected, identifier, inspectSuperClasses, true);
    }

    /**
     * Inspect the object using reflections and store it in a tree structure of Nodes
     * @param objectToBeInspected - root object of the tree structure; 
     * @param identifier - variable name referencing the object 
     * @param inspectSuperClasses - true -> super class fields are inspected too
     * @param hideGeneratedVars - true -> compiler generated vars are hidden
     * @return instance of NodeGenerator
     */
    public static NodeGenerator inspect(Object objectToBeInspected, String identifier, boolean inspectSuperClasses, boolean hideGeneratedVars) {
        assert !objectToBeInspected.getClass().getPackageName().startsWith("java") : "Can't inspect Java owned objects!";
        NodeGenerator g = new NodeGenerator();
        g.hideGeneratedVars = hideGeneratedVars;
        g.inspectSuperClasses = inspectSuperClasses;
        g.root = g.objectReferenceToNodeTree(objectToBeInspected, identifier, true, false);
        return g;
    }

    /**
     * Convert Node tree into a dot graph and save it in the working directory
     * @param root - root node of the Node tree
     */
    public void toGraph() {
        String dotSource = "digraph G {\n" + root.toString() + "}";
        File dot;
        byte[] img_stream = null;
        File img;
        try {
            dot = writeDotSourceToFile(dotSource);
            if (dot != null) {
                img = File.createTempFile("graph_", ".png", new File("./"));
                Runtime rt = Runtime.getRuntime();
                String[] cmd = {"dot", "-Tpng", dot.getAbsolutePath(), "-o", img.getAbsolutePath()};
                Process p = rt.exec(cmd);
                p.waitFor();
               // dot.delete();   // Delete dot file - remove this line to view the dot file
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public ObjectNode_425 root() {
        return root;
    }

    @Override
    public String toString() {
        return root.toString();
    }

    private Field[] combineFields(Class classToBeInspected, Field[] fields) {
        Field[] classFields = classToBeInspected.getDeclaredFields();
        Field[] combinedFields = new Field[fields.length + classFields.length];
        for (int i = 0; i < combinedFields.length; i++) {
            combinedFields[i] = i < fields.length ? fields[i] : classFields[i - fields.length];
        }

        Class superclass = classToBeInspected.getSuperclass();
        if (superclass != null && inspectSuperClasses) return combineFields(superclass, combinedFields);
        return combinedFields;
    }

    private ObjectNode_425 objectReferenceToNodeTree(Object objectToBeInspected, String identifier, boolean isRoot, boolean isDotted) {
        Class classToBeInspected = objectToBeInspected.getClass();

        // reuse same node for identical objects
        if (inspectedObject.keySet().stream().anyMatch(key -> key == objectToBeInspected)) {
            return new ChildNode_425(inspectedObject.get(objectToBeInspected).name, inspectedObject.get(objectToBeInspected).value, identifier, inspectedObject.get(objectToBeInspected).isDotted);
        }

        ObjectNode_425 result = isRoot 
            ? new RootNode_425("n"+nodeCounter++, Optional.of(classToBeInspected.getSimpleName()), identifier) 
            : new ChildNode_425("n"+nodeCounter++, Optional.of(classToBeInspected.getSimpleName()), identifier, isDotted);
        
        // Identify when the same object is used
        inspectedObject.put(objectToBeInspected, result);

        Field[] fields = combineFields(classToBeInspected, new Field[0]);
        ObjectNode_425[] childs = new ObjectNode_425[fields.length];

        for(int i = 0; i < fields.length; i++) {
            if (!fields[i].getName().startsWith("$") && !fields[i].canAccess(objectToBeInspected)) 
                continue;    //ignore inaccessible fields
            if (fields[i].getName().startsWith("$") && hideGeneratedVars)
                continue;   //ignore intern vars

            try {
                Object fieldObj = fields[i].get(objectToBeInspected);
                if (fieldObj != null) {
                    // reuse same node for identical fields
                    if (inspectedObject.keySet().stream().anyMatch(key -> key == fieldObj)) {
                        childs[i] = new ChildNode_425(inspectedObject.get(fieldObj).name, inspectedObject.get(fieldObj).value, fields[i].getName(), !Arrays.asList(classToBeInspected.getDeclaredFields()).contains(fields[i]));
                        continue;
                    }

                    // special cases like array, collections and maps
                    if (fields[i].getType().isArray()) {
                        childs[i] = processArray(fieldObj, fields[i].getName(), Optional.empty(), !Arrays.asList(classToBeInspected.getDeclaredFields()).contains(fields[i]));
                        continue;
                    }
                    if (Collection.class.isAssignableFrom(fieldObj.getClass())) {
                        childs[i] = processArray(((Collection)fieldObj).toArray(), fields[i].getName(), Optional.empty(), !Arrays.asList(classToBeInspected.getDeclaredFields()).contains(fields[i]));
                        childs[i].value = Optional.of(fieldObj.getClass().getSimpleName());
                        continue;
                    }
                    if (Map.class.isAssignableFrom(fieldObj.getClass())) {
                        childs[i] = processArray(((Map)fieldObj).values().toArray(), fields[i].getName(), 
                            Optional.of(((Map)fieldObj).keySet().toArray()), !Arrays.asList(classToBeInspected.getDeclaredFields()).contains(fields[i]));
                        childs[i].value = Optional.of(fieldObj.getClass().getSimpleName());
                        continue;
                    }
                }

                // regular values / objects
                childs[i] = processTypes(fields[i].getType().getTypeName(), fieldObj, fields[i].getName(), !Arrays.asList(classToBeInspected.getDeclaredFields()).contains(fields[i]));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        result.children = childs;
        return result;
    }

    private ObjectNode_425 processTypes(String typename, Object obj, String identifier, boolean isDotted) {
        // special cases for primitive types and strings
        return switch (typename) {
            case "int", "java.lang.Integer" -> new ChildNode_425("n" + nodeCounter++, Optional.of(((Integer)obj).toString()), identifier, isDotted);
            case "boolean", "java.lang.Boolean" -> new ChildNode_425("n" + nodeCounter++, Optional.of(((Boolean)obj).toString()), identifier, isDotted);
            case "float", "java.lang.Float" -> new ChildNode_425("n" + nodeCounter++, Optional.of(((Float)obj).toString()), identifier, isDotted);
            case "double", "java.lang.Double" -> new ChildNode_425("n" + nodeCounter++, Optional.of(((Double)obj).toString()), identifier, isDotted);
            case "char", "java.lang.Character" -> new ChildNode_425("n" + nodeCounter++, Optional.of(((Character)obj).toString()), identifier, isDotted);
            case "String", "java.lang.String" -> new ChildNode_425("n" + nodeCounter++, Optional.of("\\\"" + ((String)obj) + "\\\""), identifier, isDotted);
            default -> (obj != null)  // if object is null display it as point
                ? (!obj.getClass().getPackageName().startsWith("java") // recursivly travel through objects that are not part of java
                    ? objectReferenceToNodeTree(obj, identifier, false, isDotted)  
                    : new ChildNode_425("n" + nodeCounter++, Optional.of(obj.getClass().getSimpleName()), identifier, isDotted)) 
                : new ChildNode_425("n" + nodeCounter++, Optional.empty(), identifier, isDotted);
        };
    }

    private ObjectNode_425 processArray(Object obj, String identifier, Optional<Object[]> index, boolean isDotted) {
        int arrayLength = Array.getLength(obj);
        ObjectNode_425[] arrayChilds = new ObjectNode_425[arrayLength];
        for (int j = 0; j < arrayLength; j++) {
            Object element = Array.get(obj, j);
            ObjectNode_425 child = (element != null) 
                ? (element.getClass().getPackageName().startsWith("java") // recursivly travel through objects that are not part of java
                    ? processTypes(element.getClass().getTypeName(), element, index.isPresent() //display regular index or custom one for e.g. maps
                        ? index.get()[j].toString() 
                        : Integer.valueOf(j).toString(), isDotted) 
                    : objectReferenceToNodeTree(element, index.isPresent() //display regular index or custom one for e.g. maps
                        ? index.get()[j].toString() 
                        : Integer.valueOf(j).toString(), false, isDotted))
                : new ChildNode_425("n" + nodeCounter++, Optional.empty(), index.isPresent() //display regular index or custom one for e.g. maps
                        ? index.get()[j].toString() 
                        : Integer.valueOf(j).toString(), isDotted);
            arrayChilds[j] = child;
        }
        return new ArrayNode("n" + nodeCounter++, Optional.of(obj.getClass().getSimpleName()), identifier, arrayLength, isDotted, arrayChilds);
    }

    private File writeDotSourceToFile(String str) throws IOException {
        File temp = File.createTempFile("temp", ".dot", new File("./"));
        FileWriter fw = new FileWriter(temp);
        fw.write(str);
        fw.close();
        return temp;
    }
}
enum Font { 
    ARIAL("Arial"),
    VERDANA("Verdana"),
    TIMES("Times New Roman"),
    COURIER("Courier New"),
    SERIF("serif"),
    SANSSERIF("sans-serif");

    final String fullName; 

    private Font(String fullName) { this.fullName = fullName; }
    
    public String toString() { return fullName;}

    static enum Align {
        CENTER, LEFT, RIGHT;
        public String toString() { return name().toLowerCase(); }    
    }
}
class Turtle implements Clerk {
    final String ID;
    LiveView view;
    final int width, height;
    Font textFont = Font.SANSSERIF;
    double textSize = 10;
    Font.Align textAlign = Font.Align.CENTER;

    Turtle(LiveView view, int width, int height) {
        this.view = view;
        this.width  = Math.max(1, Math.abs(width));  // width is at least of size 1
        this.height = Math.max(1, Math.abs(height)); // height is at least of size 1
        ID = Clerk.getHashID(this);
        Clerk.load(view, "views/Turtle/turtle.js");
        Clerk.write(view, "<canvas id='turtleCanvas" + ID + "' width='" + this.width + "' height='" + this.height + "' style='border:1px solid #000;'></canvas>");
        Clerk.script(view, "const turtle" + ID + " = new Turtle(document.getElementById('turtleCanvas" + ID + "'));");
    }

    Turtle(LiveView view) { this(view, 500, 500); }
    Turtle(int width, int height) { this(Clerk.view(), width, height); }
    Turtle() { this(Clerk.view()); }

    Turtle penDown() {
        Clerk.call(view, "turtle" + ID + ".penDown();");
        return this;
    }

    Turtle penUp() {
        Clerk.call(view, "turtle" + ID + ".penUp();");
        return this;
    }

    Turtle forward(double distance) {
        Clerk.call(view, "turtle" + ID + ".forward(" + distance + ");");
        return this;
    }

    Turtle backward(double distance) {
        Clerk.call(view, "turtle" + ID + ".backward(" + distance + ");");
        return this;
    }

    Turtle left(double degrees) {
        Clerk.call(view, "turtle" + ID + ".left(" + degrees + ");");
        return this;
    }

    Turtle right(double degrees) {
        Clerk.call(view, "turtle" + ID + ".right(" + degrees + ");");
        return this;
    }

    Turtle color(int red, int green, int blue) {
        Clerk.call(view, "turtle" + ID + ".color('rgb(" + (red & 0xFF) + ", " + (green & 0xFF) + ", " + (blue & 0xFF) + ")');");
        return this;
    }

    Turtle color(int rgb) {
        color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
        return this;
    }

    Turtle lineWidth(double width) {
        Clerk.call(view, "turtle" + ID + ".lineWidth('" + width + "');");
        return this;
    }

    Turtle reset() {
        Clerk.call(view, "turtle" + ID + ".reset();");
        return this;
    }

    Turtle text(String text, Font font, double size, Font.Align align) {
        textFont = font;
        textSize = size;
        textAlign = align;
        Clerk.call(view, "turtle" + ID + ".text('" + text + "', '" + "" + size + "px " + font + "', '" + align + "')");
        return this;
    }

    Turtle text(String text) { return text(text, textFont, textSize, textAlign); }

    Turtle moveTo(double x, double y) {
        Clerk.call(view, "turtle" + ID + ".moveTo(" + x + ", " + y + ");");
        return this;
    }

    Turtle lineTo(double x, double y) {
    Clerk.call(view, "turtle" + ID + ".lineTo(" + x + ", " + y + ");");
    return this;
    }
}
record Marked(LiveView view) implements Clerk {
    public Marked {
        String onlinePath = "https://cdn.jsdelivr.net/npm/marked/marked.min.js";
        String localPath = "views/Markdown/marked.min.js";
        Clerk.load(view, onlinePath, localPath);
        Clerk.script(view, """
            var md = marked.use({
                gfm: true
            });
            """);
    }
    public String write(String markdownText) {
        String ID = Clerk.generateID(10);
        // Using `preformatted` is a hack to get a Java String into the Browser without interpretation
        Clerk.write(view, "<script id='" + ID + "' type='preformatted'>" + markdownText + "</script>");
        Clerk.call(view, "var scriptElement = document.getElementById('" + ID + "');"
        +
        """
        var divElement = document.createElement('div');
        divElement.id = scriptElement.id;
        divElement.innerHTML = md.parse(scriptElement.textContent);
        scriptElement.parentNode.replaceChild(divElement, scriptElement);
        """);
        return ID;
    }
}
record MarkdownIt(LiveView view) implements Clerk {
    public MarkdownIt {
        String onlinePath = "https://cdn.jsdelivr.net/npm/markdown-it@14.1.0/dist/markdown-it.min.js";
        String localPath = "views/Markdown/markdown-it.min.js";
        Clerk.load(view, onlinePath, localPath);
        Clerk.load(view, "views/Markdown/highlight.min.js");
        Clerk.load(view, "views/Markdown/mathjax3.js");
        // Clerk.script(view, """
        //     var md = markdownit({
        //         html: true,
        //         linkify: true,
        //         typographer: true
        //     });
        //     """);
        Clerk.script(view, """
            var md = markdownit({
                highlight: function (str, lang) {
                    if (lang && hljs.getLanguage(lang)) {
                        try {
                            return hljs.highlight(str, { language: lang }).value;
                        } catch (__) {}
                    }
                    return ''; // use external default escaping
                },
                html: true,
                linkify: true,
                typographer: true
            });
            md.use(window.mathjax3);
            """);
    }
    public String write(String markdownText) {
        String ID = Clerk.generateID(10);
        // Using `preformatted` is a hack to get a Java String into the Browser without interpretation
        Clerk.write(view, "<script id='" + ID + "' type='preformatted'>" + markdownText + "</script>");
        Clerk.call(view, "var scriptElement = document.getElementById('" + ID  + "');"
        +
        """
        var divElement = document.createElement('div');
        divElement.id = scriptElement.id;
        divElement.innerHTML = md.render(scriptElement.textContent);
        scriptElement.parentNode.replaceChild(divElement, scriptElement);
        """
        );
        return ID;
    }
}
import java.util.Arrays;
import java.util.Optional;
class TicTacToe implements Clerk {
    final String ID;
    final int width, height;
    final String libPath = "views/TicTacToe/tictactoe.js";
    LiveView view;
    
    int[] fields = {0,0,0,0,0,0,0,0,0};
    int turn = 1;

    TicTacToe(LiveView view, int width, int height) {
        this.view = view;
        this.width  = Math.max(1, Math.abs(width));  // width is at least of size 1
        this.height = Math.max(1, Math.abs(height)); // height is at least of size 1
        Clerk.load(view, libPath);
        ID = Clerk.getHashID(this);

        Clerk.write(view, "<canvas id='tttCanvas" + ID + "' width='" + this.width + "' height='" + this.height + "' style='border:1px solid #000;'></canvas>");
        Clerk.script(view, "const ttt" + ID + " = new TicTacToe(document.getElementById('tttCanvas" + ID + "'), 'ttt" + ID + "');");
        
        this.view.createResponseContext("/ttt" + ID, response -> {
            int i = Integer.parseInt(response);
            if (i >= 0 && i < 9) {
                move(i);
                int[] winnerPos = getWinnerPos();
                if (winnerPos.length == 3) {
                    this.sendWinPosition(winnerPos[0], winnerPos[2]);
                }
            }
        });
    }

    TicTacToe(LiveView view) { this(view, 500, 500); }
    TicTacToe(int width, int height) { this(Clerk.view(), width, height); }
    TicTacToe() { this(Clerk.view());}

    int[] getWinnerPos() {
        /*
            code
         */
        return new int[0];
    }

    TicTacToe sendWinPosition(int start, int end) {
        Clerk.call(view, "ttt" + ID + ".showWinner(" + start + ", " + end + ")");
        return this;
    }

    TicTacToe move(int position) {
        if (fields[position] == 0) {
            fields[position] = turn;
            Clerk.call(view, "ttt" + ID + ".drawToken(" + (turn == 1) + ", " + position + ")");
            turn = -turn;            
        }
        return this;
    }
}
class Dot implements Clerk {
    final String visLibOnlinePath = "https://unpkg.com/vis-network/standalone/umd/vis-network.min.js";
    final String visLibOfflinePath = "views/Dot/vis-network.min.js";
    final String dotLibPath = "views/Dot/dot.js";
    final String ID;
    LiveView view;
    int width, height;

    Dot(LiveView view, int width, int height) {
        this.view = view;
        this.width = width;
        this.height = height;

        Clerk.load(view, visLibOnlinePath, visLibOfflinePath);
        Clerk.load(view, dotLibPath);

        ID = Clerk.getHashID(this);

        Clerk.write(view, "<div id='dotContainer" + ID + "'></div>");
        Clerk.script(view, "const dot" + ID + " = new Dot(document.getElementById('dotContainer" + ID + "'), " + this.width + ", " + this.height + ");");
    }

    Dot(LiveView view) { this(view, 500, 500); }
    Dot(int width, int height) { this(Clerk.view(), width, height); }
    Dot() { this(Clerk.view());}

    Dot draw(String dotString) {
        String escaped = dotString.replaceAll("\\\"", "\\\\\"").replaceAll("\\n", "");
        Clerk.script(view, "dot" + ID + ".draw(\"dinetwork{" + escaped + "}\")");
        return this;
    }
}
class Slider implements Clerk {
    final String ID;
    LiveView view;
    Slider(LiveView view, double min, double max) {
        this.view = view;
        ID = Clerk.getHashID(this);
        Clerk.write(view, "<div><input type='range' id='slider" + ID + "' min='" + min + "' max='" + max + "' step='any'/> </div>");
        Clerk.script(view, "const slider" + ID + " = document.getElementById('slider" + ID + "');");
    }
    Slider attachTo(Consumer<String> delegate) {
        this.view.createResponseContext("/slider" + ID, delegate, ID);
        Clerk.script(view, Text.fillOut(
            """
            slider${0}.addEventListener('input', (event) => {
                if (locks.includes('${0}')) return;
                locks.push('${0}');
                const value = event.target.value;
                console.log(`slider${0}: value = ${value}`);
                fetch('slider${0}', {
                   method: 'post',
                    body: value.toString()
                }).catch(console.error);
            });
            """, Map.of("0", ID, "value", "${value}")));
        return this;
    }
}
LiveView view = Clerk.view();
import java.io.*; 
import models.*;
import controllers.*;
import services.*;
import ai.*;
System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
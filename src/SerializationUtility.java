import sun.misc.Unsafe; //looooooooool we c now boyz

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Easy-peasy save and restore for any type of object as long as it only contains
 * primitive fields. Will break spectacularly if your object has an array
 * or object field!!
 * @author Ronny Chan
 * @license MIT License
 */
public class SerializationUtility<T extends  Serializable> {

    private static final String OBJECT_SEPARATOR = "---";
    private static final String TYPE = "$type";
    private static final String FIELD_SEPARATOR = "|";
    private final String fileName;
    private static Unsafe unsafe;

    /**
     * Attempt to get the the low level "unsafe" object for Java malloc
     */
    static {
        try {

            Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            singleoneInstanceField.setAccessible(true);
            unsafe = (Unsafe) singleoneInstanceField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes a serialization utility
     * @param fileName The file name to save to.
     */
    public SerializationUtility(String fileName) {
        this.fileName = fileName;
    }

    /***
     * Saves the array of objects to the file name.
     * @param array The array to save
     * @throws IOException
     */
    public void save(T[] array) throws IOException {
        BufferedWriter r = new BufferedWriter(new FileWriter(this.fileName));
        r.write(array.length + "\n"); //write the array length.
        for(T o : array) { //for every object in the array
            r.write(TYPE + FIELD_SEPARATOR + o.getClass().getName()+"\n"); //write the java type identifier to the file
            for(Field f : getInheritedFields(o.getClass())) { //get all the fields
                if(Modifier.isTransient(f.getModifiers())) continue; //ignore transient fields
                if(Modifier.isStatic(f.getModifiers())) continue; //ignore static fields
                f.setAccessible(true); //force private field to public
                try {
                    r.write(f.getName() + FIELD_SEPARATOR + f.get(o)+"\n"); //write the value
                } catch (IllegalAccessException e) {
                    continue;
                }
            }
            r.write(SerializationUtility.OBJECT_SEPARATOR + "\n"); //write the separator
        }
        r.flush();
        r.close();
    }

    /**
     * Loads an array of objects from the file.
     * @param array An empty array of the type of the class, for example new Employee[] {}
     * @param <E> The type of the class, just put this with <> brackets
     * @return The loaded array.
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public <E extends T> E[] load(E[] array) throws IOException {
        List<E> tempList = new ArrayList<>();
        BufferedReader r = new BufferedReader(new FileReader(this.fileName));
        r.readLine(); //don't need the array size yet.
        for(String typeDecl; (typeDecl = r.readLine()) != null;) { //read until the end of file.
            try {
                Class classToInstantiate = Class.forName(typeDecl.substring(TYPE.length() + FIELD_SEPARATOR.length())); //Get the class name
                Object instance = classToInstantiate.cast(unsafe.allocateInstance(classToInstantiate)); //allocate the memory for the instance of the class
                HashMap<String, String> values = new HashMap<>(); //save all the values for the object
                for(String kvp; !(kvp = r.readLine()).equals(SerializationUtility.OBJECT_SEPARATOR);) //read until the end of the object marked by ---
                {
                    String[] fieldValue = kvp.split(Pattern.quote(FIELD_SEPARATOR), 2);
                    values.put(fieldValue[0], fieldValue[1]); //put the value in to the map.
                }
                for(Class<?> c = classToInstantiate; c != null; c = c.getSuperclass()) { //loop through all parent classes
                    for(Field f : c.getDeclaredFields()) {
                        if(Modifier.isTransient(f.getModifiers())) continue; //ignore transient fields
                        if(Modifier.isStatic(f.getModifiers())) continue; //ignore static fields
                        if(!values.containsKey(f.getName())) continue; //ignore if the class does not have the field
                        f.setAccessible(true); //force the field to public
                        f.set(instance, deserializePrimitive(f.getType(), values.get(f.getName()))); //set the field value

                        //I feel like a C programmer, good lord in heaven why is this even possible.
                    }
                }
                tempList.add((E)instance); //add the instance to the array
            }catch (Exception e) {
                continue;
            }
        }

        return tempList.toArray(array); //return the array.
    }

    /**
     * Turns a string into an object.
     * Basically a Class.parseClass for every type of primitive.
     * @param type The type of the object
     * @param value The string value of the object
     * @return The value.
     */
    private Object deserializePrimitive(Class type, String value) {
        if(type == String.class) return value;
        if(type.isEnum()) return Enum.valueOf(type, value);
        if(type == int.class || type == Integer.class) return Integer.parseInt(value);
        if(type == short.class || type == Short.class) return Short.parseShort(value);
        if(type == long.class || type == Long.class) return Long.parseLong(value);
        if(type == byte.class || type == Byte.class) return Byte.parseByte(value);
        if(type == char.class || type == Character.class) return value.charAt(0);
        if(type == double.class || type == Double.class) return Double.parseDouble(value);
        if(type == float.class || type == Float.class) return Float.parseFloat(value);
        if(type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        return null;
    }

    /**
     * Get all the fields of the class including it's superclasses's field
     * @param type The type of the class
     * @return All the fields of the class
     */
    private static Field[] getInheritedFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields.toArray(new Field[fields.size()]);
    }

}

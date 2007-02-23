
import java.io.File;

/**
 * Defines a speech object that can be played by the CPlayer. Contains type information,
 * and stores actual object that will be played.
 */
public class CSpeechObject {

    private CSpeechType type;
    private Object data = null;
    private String name;
    
    /**
     * Creates a JSML text SpeechObject with the given text.
     *
     * @param jsmlText the JSML text of the SpeechObject
     *
     * @return a JSML text CSpeechObject 
     */
    public static CSpeechObject createJSMLSpeech(String jsmlText) {
	return new CSpeechObject(CSpeechType.JSML, jsmlText, jsmlText);
    }

    /**
     * Creates a JSML file SpeechObject with the given File.
     *
     * @param jsmlFile the JSML file
     *
     * @return a JSML file CSpeechObject
     */
    public static CSpeechObject createJSMLFileSpeech(File jsmlFile) {
	return new CSpeechObject
	    (CSpeechType.JSML_FILE, jsmlFile, jsmlFile.getName());
    }
    
    /**
     * Creates an ASCII text SpeechObject with the given text.
     *
     * @param text the ASCII text
     *
     * @return an ASCII text JSML file CSpeechObject
     */
    public static CSpeechObject createTextSpeech(String text) {
	return new CSpeechObject(CSpeechType.TEXT, text, text);
    }

    /**
     * Creates an ASCII file SpeechObject with the given text file.
     *
     * @param textFile the ASCII text file
     *
     * @return an ASCII text file CSpeechObject
     */
    public static CSpeechObject createTextFileSpeech(File textFile) {
	return new CSpeechObject
	    (CSpeechType.TEXT_FILE, textFile, textFile.getName());
    }
    
    /**
     * Constructs a SpeechObject of the given type and data.
     *
     * @param type the CSpeechObject type
     * @param data the object containing the CSpeechObject data
     */
    private CSpeechObject(CSpeechType type, Object data, String name) {
	this.type = type;
	this.data = data;
	this.name = name;
    }

    /**
     * Returns the CSpeechObject type.
     *
     * @return the CSpeechType
     */
    public CSpeechType getType() {
	return type;
    }
    
    /**
     * Returns the File corresponding to this SpeechObject.
     * 
     * @return the CSpeechObject File
     */
    public File getFile() {
	if (type == CSpeechType.TEXT_FILE ||
	    type == CSpeechType.JSML_FILE) {
	    return (File) data;
	} else {
	    return null;
	}
    }

    /**
     * Returns the text corresponding to this SpeechObject.
     *
     * @return the CSpeechObject text
     */
    public String getText() {
	if (type == CSpeechType.JSML || type == CSpeechType.TEXT) {
	    return (String) data;
	} else {
	    return null;
	}
    }

    /**
     * Returns the name of this SpeechObject
     *
     * @return the name of this CSpeechObject
     */
    public String getName() {
	return name;
    }

    /**
     * Returns a String describing the type and name of this SpeechObject, e.g.,
     * <p><code>[JSML file] example1.jsml</code>
     *
     * @return the type and name of this CSpeechObject
     */
    public String toString() {
	String typeName = "[" + type.toString() + "] ";
	return typeName + name;
    }
    
}

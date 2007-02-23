
/**
 * Defines constants which represent different types of CSpeechObjects,
 * which can be played by the CPlayer.
 */
public class CSpeechType {

    private String typeName;

    /**
     * The ASCII text type.
     */
    public static final CSpeechType TEXT = new CSpeechType("text");

    /**
     * The ASCII text file type.
     */
    public static final CSpeechType TEXT_FILE = new CSpeechType("text file");
        
    /**
     * The JSML file type.
     */
    public static final CSpeechType JSML_FILE = new CSpeechType("JSML file");

    /**
     * The JSML text type.
     */
    public static final CSpeechType JSML = new CSpeechType("JSML");
       
     
    /**
     * Constructs a CSpeechType with the given name.
     *
     * @param typeName the CSpeechType name
     */
    private CSpeechType(String typeName) {
	this.typeName = typeName;
    }


    /**
     * Returns the name of the type.
     *
     * @return the name of the CSpeechType
     */
    public String toString() {
	return typeName;
    }
}

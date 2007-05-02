/*
This file is part of Read2Me!

Read2Me! is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

Read2Me! is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Read2Me!. If not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package textToSpeech;
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

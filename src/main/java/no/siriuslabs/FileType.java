package no.siriuslabs;

/**
 * Enum providing details on the different types of files.
 */
public enum FileType {

    ONTOLOGY("Ontology", "OWL", "*.owl"),
    SIGNATURE("Signature", "TXT", "*.txt");

    private final String name;
    private final String shortName;
    private final String fileExtension;

    FileType(String name, String shortName, String fileExtension) {
        this.name = name;
        this.shortName = shortName;
        this.fileExtension = fileExtension;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}

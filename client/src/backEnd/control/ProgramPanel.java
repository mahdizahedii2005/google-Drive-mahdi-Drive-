package backEnd.control;

public class ProgramPanel {
    private final String userName;
    private static ProgramPanel programPanel;
    private int port = 8887;

    public ProgramPanel(String userName) {
        this.userName = userName;
        programPanel = this;
        System.out.println("im enter as : " + userName);
    }

    public static ProgramPanel getProgramPanel() {
        if (programPanel == null) {
            throw new NullPointerException();
        }
        return programPanel;
    }
}

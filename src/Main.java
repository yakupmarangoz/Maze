import java.util.List;

import javax.swing.SwingUtilities;



public class Main{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new Interface().InitUI());
    }
}
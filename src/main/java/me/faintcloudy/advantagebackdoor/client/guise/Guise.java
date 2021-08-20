package me.faintcloudy.advantagebackdoor.client.guise;

import javax.swing.*;
import java.io.File;

public class Guise {

    public static void main()
    {
        JOptionPane.showMessageDialog(null, "已启用自动备份, 备份更新时将会自动推送", "提示", JOptionPane.INFORMATION_MESSAGE);
        File file = new File("backup");
        if (!file.exists())
            file.mkdir();
    }
}

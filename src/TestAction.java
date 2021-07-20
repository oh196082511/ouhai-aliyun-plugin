import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import sun.misc.BASE64Encoder;

import java.awt.*;
import java.net.URI;
import java.nio.charset.Charset;

public class TestAction extends AnAction {

    private final BASE64Encoder encoder = new BASE64Encoder();

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(true);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        String projectName = getEventProject(e).getName();
        Desktop desktop = Desktop.getDesktop();// 获取桌面
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            String url = makeUrl(projectName);
            try {
                URI uri = new URI(url);
                desktop.browse(uri);
            } catch (Exception exception) {
                System.out.println("error in test;url: " + url);
            }
        }

    }

    private String makeUrl(String project) {
        String queryString = "* and __raw__: " + project + " ";
        long startTimeInSec = System.currentTimeMillis() / 1000 - 60 * 60 * 4; // 前4小时
        long endTimeInSec = System.currentTimeMillis() / 1000;
        String url = JumpUrl.TEST_URL;
        url += "?";
        url += "encode%3Dbase64%26";// encode=base64&
        url += "queryString%3D";// queryString=
        url += encoder.encodeBuffer(queryString.getBytes(Charset.defaultCharset()));
        url = url.substring(0, url.length() - 1); // 删除最后一个\n
        url += "%26queryTimeType%3D99%26startTime%3D"; //&queryTimeType=99&startTime=
        url += startTimeInSec;
        url += "%26endTime%3D"; // &endTime=
        url += endTimeInSec;
        return url;
    }
}

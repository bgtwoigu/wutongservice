package com.borqs.server.base.tools;


import com.borqs.server.base.util.DateUtils;
import com.borqs.server.base.web.webmethod.WebMethodClient;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebTesterGui extends JFrame {

    private JTextField uriText;
    private JTextField ticketText;
    private JTextField appidText;
    private JTextField secretText;
    private JTextField methodText;
    private JCheckBox viewElapsedCheckBox;
    private JTextPane queryParamsText;

    private JTextField statusCodeText;
    private JTextArea contentText;

    public static void main(String[] args) {
        WebTesterGui frame = new WebTesterGui();
        frame.setVisible(true);
    }

    public WebTesterGui() throws HeadlessException {
        init();
        loadConfig();
    }

    private void init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveConfig();
            }
        });


        this.setTitle("Web Tester");
        this.setSize(800, 600);

        this.uriText = new JTextField();
        this.ticketText = new JTextField();
        this.appidText = new JTextField();
        this.secretText = new JTextField();
        this.methodText = new JTextField();
        this.viewElapsedCheckBox = new JCheckBox("View elapsed counter", false);
        this.queryParamsText = new JTextPane();
        this.statusCodeText = new JTextField();
        this.contentText = new JTextArea();

        this.statusCodeText.setEditable(false);
        this.contentText.setEditable(false);


        // Request
        JPanel reqPane = new JPanel();
        reqPane.setPreferredSize(new Dimension(300, -1));
        reqPane.setLayout(new BorderLayout());
        JPanel reqTopPane = new JPanel();
        JPanel reqCenterPane = new JPanel();
        JPanel reqBottomPane = new JPanel();

        reqTopPane.setLayout(new BoxLayout(reqTopPane, BoxLayout.Y_AXIS));
        reqTopPane.add(wrapLabel(uriText, "URI", 'U'));
        reqTopPane.add(wrapLabel(ticketText, "Ticket", 'T'));
        reqTopPane.add(wrapLabel(appidText, "Appid", 'i'));
        reqTopPane.add(wrapLabel(secretText, "AppSecret", 'S'));
        reqTopPane.add(wrapLabel(methodText, "Method", 'M'));
        reqTopPane.add(viewElapsedCheckBox);
        reqCenterPane.setLayout(new BorderLayout());
        reqCenterPane.add(wrapTitleBorder(wrapScroll(queryParamsText), "Query params"));

        JButton invokeButton = new JButton("Invoke!");
        invokeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doInvoke();
            }
        });
        reqBottomPane.add(invokeButton);

        JButton copyCurlButton = new JButton("Copy CURL");
        copyCurlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyCurlCommand(false);
            }
        });
        reqBottomPane.add(copyCurlButton);


        copyCurlButton = new JButton("Copy CURL For Summary");
        copyCurlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyCurlCommand(true);
            }
        });
        reqBottomPane.add(copyCurlButton);

        reqPane.add(reqTopPane, BorderLayout.NORTH);
        reqPane.add(reqBottomPane, BorderLayout.SOUTH);
        reqPane.add(reqCenterPane, BorderLayout.CENTER);


        // Response
        JPanel respPane = new JPanel();
        respPane.setLayout(new BorderLayout());

        JPanel respTopPane = new JPanel();
        JPanel respCenterPane = new JPanel();
        respTopPane.setLayout(new BorderLayout());
        respCenterPane.setLayout(new BorderLayout());

        respTopPane.add(wrapLabel(statusCodeText, "Result info", 'C'));
        respCenterPane.add(wrapTitleBorder(wrapScroll(contentText), "Content"));


        respPane.add(respTopPane, BorderLayout.NORTH);
        respPane.add(respCenterPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(reqPane);
        splitPane.setRightComponent(respPane);
        getContentPane().add(splitPane);
    }

    private static boolean isPost(Map<String, Object> params) {
        for (Object v : params.values()) {
            if (v instanceof File) {
                return true;
            }
        }
        return false;
    }

    private void doInvoke() {
        if (StringUtils.isBlank(uriText.getText())) {
            showErrorDialog("Remote is blank");
            return;
        }

        if (StringUtils.isBlank(appidText.getText())) {
            showErrorDialog("Appid is blank");
            return;
        }


        if (StringUtils.isBlank(secretText.getText())) {
            showErrorDialog("AppSecret is blank");
            return;
        }

        if (StringUtils.isBlank(methodText.getText())) {
            showErrorDialog("Method is blank");
            return;
        }

        WebMethodClient client = WebMethodClient.create(uriText.getText(), ticketText.getText(), appidText.getText(), secretText.getText());
        client.setViewElapsed(viewElapsedCheckBox.isSelected());
        Map<String, Object> params = parseParams(queryParamsText.getText());
//        HttpResponse resp = client.get(methodText.getText(), params);
//        statusCodeText.setText(Integer.toString(WebMethodClient.getResponseCode(resp)));
//
//        String json = WebMethodClient.getResponseText(resp);
//        File file = new File("/home/huhengyi/picture_unknown.png");
        //String id = photoDir.getText();
        //File file = new File(id);
        //if (file.exists())
        //    params.put("image_data", file);
        HttpResponse resp;
        boolean post = isPost(params);

        statusCodeText.setText("Reading...");
        contentText.setText("");

        try {
            long startAt = DateUtils.nowMillis();
            //post = true;
            if (!post) {
                resp = client.get(methodText.getText(), params);
            } else {
                //resp = client.formPost(methodText.getText(), params);
                resp = client.multipartPost(methodText.getText(), params);
            }

            WebMethodClient.SizeInfo si = new WebMethodClient.SizeInfo();
            int status = WebMethodClient.getResponseCode(resp);
            String json = WebMethodClient.getResponseText(resp, si);
            long elapsed = DateUtils.nowMillis() - startAt;
            //statusCodeText.setText(Integer.toString(WebMethodClient.getResponseCode(resp)) + "    " + elapsed + "ms");
            statusCodeText.setText(String.format("Status: %d | Time: %dms | size: %sB | compressed: %sB",
                    status, elapsed, si.size, si.compressedSize));
            contentText.setText(json);
        } catch (Throwable t) {
            statusCodeText.setText("Error");
            contentText.setText("");
        }
    }

    private void copyCurlCommand(boolean forTime) {
        WebMethodClient client = WebMethodClient.create(uriText.getText(), ticketText.getText(), appidText.getText(), secretText.getText());
        client.setViewElapsed(viewElapsedCheckBox.isSelected());
        Map<String, Object> params = parseParams(queryParamsText.getText());
        int flags = WebMethodClient.CURL_FLAG_COMPRESSED;
        if (forTime)
            flags |= WebMethodClient.CURL_FLAG_FOR_TIME;

        String cmd;
        if (isPost(params)) {
            cmd = client.curlCommandForMultipartPost(methodText.getText(), flags, params);
        } else {
            cmd = client.curlCommandForGet(methodText.getText(), flags, params);
        }

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(cmd), new ClipboardOwner() {
            @Override
            public void lostOwnership(Clipboard clipboard, Transferable contents) {
            }
        });
    }


    private static JComponent wrapTitleBorder(JComponent c, String title) {
        c.setBorder(BorderFactory.createTitledBorder(title));
        return c;
    }

    private static JScrollPane wrapScroll(JComponent c) {
        return new JScrollPane(c);
    }

    private static JComponent wrapLabel(JComponent c, String label, char mnemonic) {
        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());
        JLabel l = new JLabel(label);
        l.setPreferredSize(new Dimension(100, -1));
        pane.add(l, BorderLayout.WEST);
        l.setLabelFor(c);
        if (mnemonic != 0) {
            l.setDisplayedMnemonic(mnemonic);
        }
        pane.add(c, BorderLayout.CENTER);
        return pane;
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private static Map<String, Object> parseParams(String s) {
        LinkedHashMap<String, Object> m = new LinkedHashMap<String, Object>();
        String[] lines = StringUtils.split(s, '\n');
        for (String line : lines) {
            String n = StringUtils.strip(StringUtils.substringBefore(line, "="));
            String v = StringUtils.strip(StringUtils.substringAfter(line, "="));
            Object vv = v;
            if (StringUtils.startsWith(v, "@")) {
                vv = new File(StringUtils.removeStart(v, "@"));
            }
            m.put(n, vv);
        }
        return m;
    }


    private void saveConfig() {
        TesterSetting setting = new TesterSetting();
        setting.uri = StringUtils.trimToNull(uriText.getText());
        setting.ticket = StringUtils.trimToNull(ticketText.getText());
        setting.appid = StringUtils.trimToNull(appidText.getText());
        setting.secret = StringUtils.trimToNull(secretText.getText());
        try {
            setting.save();
        } catch (Exception e) {
            showErrorDialog("Save configuration error");
        }
    }

    public void loadConfig() {
        try {
            TesterSetting setting = new TesterSetting();
            setting.load();
            uriText.setText(StringUtils.trimToEmpty(setting.uri));
            ticketText.setText(StringUtils.trimToEmpty(setting.ticket));
            appidText.setText(StringUtils.trimToEmpty(setting.appid));
            secretText.setText(StringUtils.trimToEmpty(setting.secret));
        } catch (Exception e) {
            showErrorDialog("Load configuration error");
        }
    }

}

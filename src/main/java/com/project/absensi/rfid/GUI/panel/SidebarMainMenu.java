package com.project.absensi.rfid.GUI.panel;

import com.project.absensi.rfid.GUI.AdminPage;
import com.project.absensi.rfid.GUI.AttendancePage;
import com.project.absensi.rfid.service.I18nService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author ACER
 */
public class SidebarMainMenu extends JPanel implements I18nService.I18nChangeListener {

    private final Color SIDEBAR_BG = new Color(30, 41, 59);
    private final Color MENU_BG = new Color(51, 65, 85);
    private final Color SUBMENU_BG = new Color(15, 23, 42);
    private final Color HOVER_BG = new Color(37, 99, 235);
    private final Color ACTIVE_BG = new Color(59, 130, 246);
    private final Color TEXT_COLOR = Color.WHITE;

    private JButton activeButton = null;

    // List pembantu untuk melacak tombol dan I18n key-nya agar bisa di-update nanti
    private final List<I18nButtonRef> localizedButtons = new ArrayList<>();

    // Kelas internal sederhana untuk menyimpan pasangan Tombol dan Key I18n-nya
    private static class I18nButtonRef {
        JButton button;
        String i18nKey;

        I18nButtonRef(JButton button, String i18nKey) {
            this.button = button;
            this.i18nKey = i18nKey;
        }
    }

    public SidebarMainMenu() {
        this.setPreferredSize(new Dimension(260, 0));
        this.setBackground(new Color(33, 37, 41));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // PERUBAHAN: Gunakan Key properti i18n, BUKAN string judul langsung!
        // Pastikan key ini sudah terdaftar di messages_id.properties, messages_en.properties, dll.

        // DATA MASTER SECTION
        this.add(createAccordion(
                "ui.sidebar.masterdata",
                new String[]{"ui.sidebar.student", "ui.sidebar.log", "ui.sidebar.user"}
        ));

        // ATTENDANCE SECTION
        this.add(createAccordion(
                "ui.sidebar.attendance",
                new String[]{"ui.sidebar.kiosk", "ui.sidebar.history", "ui.sidebar.analytics"}
        ));

        // SETTINGS SECTION
        this.add(createAccordion(
                "ui.sidebar.settings",
                new String[]{"ui.sidebar.general", "ui.sidebar.security"}
        ));

        // REPORT SECTION
        this.add(createAccordion(
                "ui.sidebar.report",
                new String[]{"ui.sidebar.reportlog", "ui.sidebar.performance"}
        ));

        this.add(Box.createVerticalGlue());

        // Panggil update teks pertama kali saat inisialisasi awal
        updateMenuTexts();

        I18nService.registerListener(SidebarMainMenu.this);
    }

    private JPanel createAccordion(String headerKey, String[] menuKeys) {

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(33, 37, 41));

        // HEADER BUTTON (Gunakan key sebagai teks sementara)
        JButton header = new JButton(headerKey);

        header.setFocusPainted(false);
        header.setBackground(MENU_BG);
        header.setForeground(TEXT_COLOR);
        header.setBorder(new EmptyBorder(15, 15, 15, 15));
        header.setHorizontalAlignment(SwingConstants.LEFT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Daftarkan header button ke list pelacak lokalisasi
        localizedButtons.add(new I18nButtonRef(header, headerKey));

        // BODY PANEL
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(SIDEBAR_BG);

        for (String menuKey : menuKeys) {

            JButton btn = new JButton(menuKey);

            btn.setFocusPainted(false);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            btn.setBackground(SUBMENU_BG);
            btn.setForeground(TEXT_COLOR);
            btn.setBorder(new EmptyBorder(10, 20, 10, 10));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Daftarkan submenu button ke list pelacak lokalisasi
            localizedButtons.add(new I18nButtonRef(btn, menuKey));

            // HOVER EFFECT (diperbaiki agar tombol aktif tidak tertimpa warna hover)
            btn.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (activeButton != btn) btn.setBackground(HOVER_BG);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (activeButton != btn) btn.setBackground(SUBMENU_BG);
                }
            });

            // ACTION ROUTING (Gunakan menuKey asli sebagai penentu kondisi routing)
            btn.addActionListener(e -> {

                switch (menuKey) {

                    case "ui.sidebar.student":
                        showPage(new SiswaPanel());
                        break;

                    case "ui.sidebar.log":
                        showPage(null);
                        break;

                    case "ui.sidebar.user":
                        showPage(null);
                        break;

                    case "ui.sidebar.kiosk":
                        try {
                            System.out.println("STEP 1 - Klik TAPKARTU");
                            AttendancePage page = new AttendancePage();
                            System.out.println("STEP 2 - AttendancePage berhasil dibuat");

                            showPage(page);
                            System.out.println("STEP 3 - AttendancePage ditampilkan");
                        } catch (Exception ex) {
                            System.out.println("ERROR SAAT MEMBUKA ATTENDANCE");
                            ex.printStackTrace();
                        }
                        break;

                    case "ui.sidebar.general":
                        showPage(new Settings(0)); // Membuka panel Settings, tab General
                        break;

                    case "ui.sidebar.security":
                        showPage(new Settings(1)); // Membuka panel Settings, tab Security
                        break;

                    default:
                        showPage(null);
                        break;
                }

                // RESET OLD ACTIVE BUTTON
                if (activeButton != null) {
                    activeButton.setBackground(SUBMENU_BG);
                }

                // SET NEW ACTIVE BUTTON
                activeButton = btn;
                btn.setBackground(ACTIVE_BG);
            });

            body.add(btn);
        }

        // DEFAULT COLLAPSE
        body.setVisible(false);

        header.addActionListener(e -> {
            body.setVisible(!body.isVisible());
            container.revalidate();
            container.repaint();
        });

        container.add(header);
        container.add(body);

        return container;
    }

    /**
     * Method ini melakukan looping untuk mengambil teks terjemahan terbaru
     * berdasarkan key yang terdaftar di dalam list pembantu.
     */
    private void updateMenuTexts() {
        for (I18nButtonRef ref : localizedButtons) {
            ref.button.setText(I18nService.get(ref.i18nKey));
        }
    }

    private void showPage(Component comp) {
        switch (comp) {
            case JPanel pnl -> {
                AdminPage.appContentPane.removeAll();
                AdminPage.appContentPane.add(pnl, BorderLayout.CENTER);

                AdminPage.appContentPane.revalidate();
                AdminPage.appContentPane.repaint();
            }
            case JFrame frm -> {
                JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(SidebarMainMenu.this);
                if (mainFrame != null) {
                    mainFrame.dispose(); // Menutup AdminPage utama saat beralih ke halaman full screen Tap Kartu
                }

                frm.setExtendedState(Frame.MAXIMIZED_BOTH);
                frm.setVisible(true);
            }
            default -> {
                // Mengosongkan halaman jika melemparkan nilai null
                AdminPage.appContentPane.removeAll();
                AdminPage.appContentPane.revalidate();
                AdminPage.appContentPane.repaint();
            }
        }
    }

    // TRIGGER TERPICU DI SINI KETIKA BAHASA DIUBAH DI APLIKASI
    public void onLanguageChanged() {
        SwingUtilities.invokeLater(() -> {
            // 1. Eksekusi proses penggantian teks semua tombol menu
            updateMenuTexts();

            // 2. Refresh struktur visual komponen
            this.revalidate();
            this.repaint();
        });
    }
}
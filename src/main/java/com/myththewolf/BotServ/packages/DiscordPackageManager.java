package com.myththewolf.BotServ.packages;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myththewolf.BotServ.packages.downloader.FileDownloader;
import com.myththewolf.BotServ.packages.downloader.Utils;

public class DiscordPackageManager {
    private JSONArray repos;
    private JSONObject runconf;
    private List<PackageRepo> actualRepos = new ArrayList<>();
    private Scanner S;

    public DiscordPackageManager(JSONObject runconf) throws JSONException, IOException {
        this.runconf = runconf;
        repos = runconf.getJSONArray("repos");
        for (int i = 0; i < this.repos.length(); i++) {
            this.actualRepos.add(new PackageRepo(this.repos.getString(i)));
        }
        S = new Scanner(System.in);
    }

    public boolean installPackage(String name, boolean update) {
        try {
            List<PackageEntry> needed = new ArrayList<>();
            String get = "";
            long totalByteDependencies = 0;
            for (PackageEntry e : this.getPackage(name).getDependencies()) {
                File OUT = new File(System.getProperty("user.dir") + File.separator + "run" + File.separator + "plugins"
                        + File.separator + e.getName() + ".jar");
                if (!OUT.exists() || update) {
                    get += "File:" + e.getReleases().get(0).getFileURL() + "\n";
                    needed.add(e);
                    totalByteDependencies = totalByteDependencies
                            + (FileDownloader.getFileSize(new URL(e.getReleases().get(0).getFileURL())));
                }
            }
            if (needed.size() > 0) {
                System.out.println("Unmet dependencies: ");
                System.out.println(get);
                System.out.println("➡This will require " + Utils.humanReadableByteCount(totalByteDependencies, false)
                        + " of disk space.");
                System.out.print("➡Download them now?[Y for yes, anything else for no]");
                String input = S.nextLine();
                if (!input.toLowerCase().equals("y")) {
                    System.out.println("Aborting.");
                    return true;
                } else {
                    List<File> outs = new ArrayList<>();
                    List<String> stringSet = new ArrayList<>();
                    List<URL> URLs = new ArrayList<>();
                    for (PackageEntry e : needed) {
                        File OUT = new File(System.getProperty("user.dir") + File.separator + "run" + File.separator
                                + "plugins" + File.separator + e.getName() + ".jar");
                        URL IN = new URL(e.getReleases().get(0).getFileURL());
                        String text = "Downloading file: " + IN.toString();
                        URLs.add(IN);
                        outs.add(OUT);
                        stringSet.add(text);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("A error has occured during package configuration. Aborted.");
            return false;
        }
        return true;
    }

    public void addRepo(String URL) {
        if (checkRepo(URL)) {
            System.out.println("➡ Checking repo..");
            this.repos.put(URL);
            saveFile();
        }
    }

    public boolean checkRepo(String URL) {
        try {

            PackageRepo rep = new PackageRepo(URL);
            System.out.println("➡Reading database..");
            int total = 0;
            int total_dependencies = 0;
            int total_differnt_repos = 0;
            List<PackageRepo> gotRepos = new ArrayList<>();
            for (PackageEntry e : rep.getPackages()) {
                total++;
                for (PackageEntry ee : e.getDependencies()) {
                    total_dependencies++;
                    if (!gotRepos.contains(ee.getRepo())) {
                        gotRepos.add(e.getRepo());
                        total_differnt_repos++;
                    }
                }
            }
            System.out.println(total + " new Packges, with " + total_dependencies + " dependencies in "
                    + total_differnt_repos + " repos.");
        } catch (Exception e) {
            System.err.println("Error in Repo parsing.. aborting!");
            return false;
        }
        return true;

    }

    public void saveFile() {
        System.out.println("Saving file....");
        Utils.writeToFile(this.runconf.toString(), new File("run/settings.json"));
    }

    public PackageEntry getPackage(String pName) throws JSONException, IOException {
        for (PackageRepo pe : actualRepos) {
            System.out.println(pe.getPackages().get(0).getPackcageBase());
            if (pe.containsPackage(pName)) {
                return pe.getPackage(pName);
            }
        }
        System.out.println("NPF:" + pName);
        return null;
    }
}

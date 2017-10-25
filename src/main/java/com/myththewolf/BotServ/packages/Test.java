package com.myththewolf.BotServ.packages;

public class Test {
    public static void main(String[] args) {
        try {
        PackageRepo PR = new PackageRepo("http://70.139.52.7/pluginrepo/");
        PR.getPackages().get(0).getDependencies().forEach(dep -> System.out.println(dep.getName()));
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

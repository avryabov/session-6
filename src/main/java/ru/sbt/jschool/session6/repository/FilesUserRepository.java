package ru.sbt.jschool.session6.repository;

import ru.sbt.jschool.session6.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FilesUserRepository implements UserRepository {
    private final static String DIR = "d:/db";
    File folder = new File(DIR);


    @Override
    public User save(User user) {
        System.out.println("SAVE");
        System.out.println(user);
        int index = currentIndex();
        user.setId(index);
        writeObj(new File(folder, index+".bin"), user);
        return user;
    }

    @Override
    public boolean delete(int id) {
        System.out.println("DELETE");
        List<File> fileList = fileList();
        for (int i = 0; i < fileList.size(); i++)
        {
            File entry = fileList.get(i);
            if(entry.getName().equals(id+".bin")) {
                entry.delete();
                return true;
            }
        }
        return false;
    }

    @Override
    public User get(int id) {
        System.out.println("GET");
        List<File> fileList = fileList();
        for (int i = 0; i < fileList.size(); i++)
        {
            File entry = fileList.get(i);
            if(entry.getName().equals(id+".bin")) {
                return readObj(entry);
            }
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        System.out.println("LIST");
        List<User> list = new ArrayList<>();
        List<File> fileList = fileList();
        for (int i = 0; i < fileList.size(); i++)
        {
            File entry = fileList.get(i);
            list.add(readObj(entry));
        }
        return list;
    }


    private int currentIndex() {
        List<File> fileList = fileList();
        for (int i = 0; i < fileList.size(); i++)
        {
            File entry = fileList.get(i);
            if(!entry.getName().equals(i+".bin"))
                return i;
        }
        return fileList.size();
    }

    private List<File> fileList() {
        File[] folderEntries = folder.listFiles();
        ArrayList<File> fileList = new ArrayList<>(Arrays.asList(folderEntries));
        fileList.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                int idx1 = Integer.parseInt(o1.getName().replaceFirst("[.][^.]+$", ""));
                int idx2 = Integer.parseInt(o2.getName().replaceFirst("[.][^.]+$", ""));
                return Integer.compare(idx1, idx2);
            }
        });
        return fileList;
    }

    private void writeObj(File file, User user) {
        try(FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User readObj(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream oin = new ObjectInputStream(fis);) {
            return (User) oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}

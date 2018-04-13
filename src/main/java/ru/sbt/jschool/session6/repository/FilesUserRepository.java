package ru.sbt.jschool.session6.repository;

import ru.sbt.jschool.session6.model.User;
import ru.sbt.jschool.session6.util.exception.NotFoundException;

import java.io.*;
import java.util.*;

public class FilesUserRepository implements UserRepository {
    private File folder;

    public FilesUserRepository(String path) {
        this.folder = new File(path);
        if(!folder.exists())
            folder.mkdir();
    }

    @Override
    public User save(User user) {
        int index = currentIndex();
        user.setId(index);
        writeObj(new File(folder, index + ".bin"), user);
        return user;
    }

    @Override
    public boolean delete(int id) {
        List<File> fileList = fileList();
        for (int i = 0; i < fileList.size(); i++) {
            File entry = fileList.get(i);
            if (entry.getName().equals(id + ".bin")) {
                return entry.delete();
            }
        }
        return false;
    }

    @Override
    public User get(int id) throws NotFoundException {
        List<File> fileList = fileList();
        for (int i = 0; i < fileList.size(); i++) {
            File entry = fileList.get(i);
            if (entry.getName().equals(id + ".bin")) {
                return readObj(entry);
            }
        }
        throw new NotFoundException("User with id = " + id + " not found");
    }

    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        List<File> fileList = fileList();
        for (int i = 0; i < fileList.size(); i++) {
            File entry = fileList.get(i);
            list.add(readObj(entry));
        }
        return list;
    }


    private int currentIndex() {
        List<File> fileList = fileList();
        for (int i = 0; i < fileList.size(); i++) {
            File entry = fileList.get(i);
            if (!entry.getName().equals(i + ".bin"))
                return i;
        }
        return fileList.size();
    }

    private List<File> fileList() {
        File[] folderEntries = folder.listFiles();
        if(folderEntries == null)
            return Collections.emptyList();
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
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User readObj(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream oin = new ObjectInputStream(fis)) {
            return (User) oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}

package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        boolean exit = false;
        do {
            List<ToDo> toDos = getAllToDos();
            updateToDos(toDos);

            int menuChoice = menu();

            switch (menuChoice) {
                case 0:
                    System.out.println(Line);
                    System.out.println("Your ToDos");
                    System.out.println(Line);
                    showMultToDos(toDos);
                case 1:
                    System.out.println(Line);
                    System.out.println("Today's ToDos");
                    System.out.println(Line);
                    getTodaysToDos(toDos);
                    break;
                case 2:
                    System.out.println(Line);
                    createToDo(toDos);
                    break;
                case 3:
                    System.out.println(Line);
                    toggleToDoCompleted(toDos);
                    break;
                case 4:
                    System.out.println(Line);
                    searchToDos(toDos);
                    break;
                case 5:
                    System.out.println(Line);
                    editToDo(toDos);
                    break;
                case 6:
                    System.out.println(Line);
                    deleteToDo(toDos);
                    break;
                case 7:
                    System.out.println("Exiting....");
                    exit = true;
                    break;
            }
        } while (!exit);
    }

    private static void deleteToDo(List<ToDo> toDos) {
        Scanner sc = new Scanner(System.in);

        int id = 0;
        boolean valid = false;
        do {
            try {
                System.out.printf("Enter the toDo id you want to delete: ");
                id = sc.nextInt();
                if (getToDoById(toDos, id) == null) {
                    valid = false;
                } else {
                    valid = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid input!");
                valid = false;
                sc.nextLine();
            }
        } while (!valid);

        boolean exit = false;
        int i = 0;
        while (!exit && i < toDos.size()) {
            if (toDos.get(i).getId() == id) {
                toDos.remove(i);
                exit = true;
            } else {
                i++;
            }
        }

        updateToDos(toDos);
    }

    private static void editToDo(List<ToDo> toDos) {
        Scanner sc = new Scanner(System.in);

        int id = 0;
        boolean valid = false;
        do {
            try {
                System.out.printf("Enter the toDo id you want to edit: ");
                id = sc.nextInt();
                if (getToDoById(toDos, id) == null) {
                    valid = false;
                    System.out.println("Enter an existing toDo");
                } else {
                    valid = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid input!");
                valid = false;
                sc.nextLine();
            }
        } while (!valid);


        System.out.println("What do you want to edit?");
        System.out.println("1. Name");
        System.out.println("2. Description");
        System.out.println("3. DueDate");

        int choice = 0;
        valid = false;
        do {
            try {
                System.out.printf("Enter your choice (1-3): ");
                choice = sc.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Please enter a number between 1 and 3");
                    valid = false;
                } else {
                    valid = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid input (1-3)!");
                valid = false;
                sc.nextLine();
            }
        } while (!valid);

        sc.nextLine();

        boolean exit = false;
        int i = 0;
        switch (choice) {
            case 1:
                System.out.printf("Please enter the new name for your task: ");
                String name = sc.nextLine();

                while (!exit && i < toDos.size()) {
                    if (toDos.get(i).getId() == id) {
                        toDos.get(i).setName(name);
                        exit = true;
                    } else {
                        i++;
                    }
                }
                break;
            case 2:
                System.out.printf("Please enter the new description for your task: ");
                String description = sc.nextLine();

                while (!exit && i < toDos.size()) {
                    if (toDos.get(i).getId() == id) {
                        toDos.get(i).setDescription(description);
                        exit = true;
                    } else {
                        i++;
                    }
                }
                break;
            case 3:
                String dueDate = "";
                String regex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])\\s([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

                valid = false;
                do {
                    System.out.printf("Enter the new due date (YYYY-MM-DD HH:mm:ss): ");
                    dueDate = sc.nextLine();
                    if (dueDate.matches(regex)) {
                        valid = true;
                    } else {
                        System.out.println("Please enter the date with the correct format");
                        valid = false;
                    }
                } while (!valid);

                while (!exit && i < toDos.size()) {
                    if (toDos.get(i).getId() == id) {
                        toDos.get(i).setDueDate(dueDate);
                        exit = true;
                    } else {
                        i++;
                    }
                }
                break;
        }

        showToDo(toDos.get(i));

        updateToDos(toDos);
    }

    private static void updateToDos(List<ToDo> toDos) {
        sortToDosByDueDate(toDos);
        saveAllToDos(toDos);
    }

    private static void searchToDos(List<ToDo> toDos) {
        Scanner sc = new Scanner(System.in);

        System.out.printf("Please enter the search term: ");
        String constraint = sc.nextLine();

        List<ToDo> filteredToDos = getToDosBy(toDos, constraint);

        showMultToDos(filteredToDos);
        if (filteredToDos.isEmpty()){
            System.out.println("ToDos not found");
        }
    }

    public static int menu() {
        Scanner sc = new Scanner(System.in);

        System.out.println(Line);
        System.out.println("What do you want to do?");
        System.out.println(Line);
        System.out.println("0. Show all toDos");
        System.out.println("1. Show today's toDos");
        System.out.println("2. Add new toDo");
        System.out.println("3. Toggle toDo completed");
        System.out.println("4. Search toDo");
        System.out.println("5. Edit toDo");
        System.out.println("6. Delete toDo");
        System.out.println(Line);
        System.out.println("7. Exit");
        System.out.println(Line);

        int choice = 0;
        boolean valid = false;
        do {
            try {
                System.out.printf("Enter your choice (0-7): ");
                choice = sc.nextInt();
                if (choice < 0 || choice > 7) {
                    System.out.println("Please enter a number between 0 and 7");
                    valid = false;
                } else {
                    valid = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid option (0-7)!");
                valid = false;
                sc.nextLine();
            }
        } while (!valid);

        return choice;
    }

    public static void toggleToDoCompleted(List<ToDo> toDos) {
        Scanner sc = new Scanner(System.in);

        int id = 0;
        boolean valid = false;
        do {
            try {
                System.out.printf("Enter the toDo id: ");
                id = sc.nextInt();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid input!");
                valid = false;
            }
        } while (!valid);

        boolean exit = false;
        int i = 0;
        while (!exit && i < toDos.size()) {
            if (toDos.get(i).getId() == id) {
                toDos.get(i).toggleCompleted();
                showToDo(toDos.get(i));
                exit = true;
            } else {
                i++;
            }
        }

        updateToDos(toDos);
    }

    public static void createToDo(List<ToDo> toDos) {
        Scanner sc = new Scanner(System.in);

        System.out.printf("Please enter a name for your task: ");
        String name = sc.nextLine();

        System.out.printf("Please enter a description for your task: ");
        String description = sc.nextLine();

        String dueDate = "";
        String regex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])\\s([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
        boolean valid = false;
        do {
                System.out.printf("Enter the due date (YYYY-MM-DD HH:mm:ss): ");
                dueDate = sc.nextLine();
                if (dueDate.matches(regex)) {
                    valid = true;
                } else {
                    System.out.println("Please enter the date with the correct format");
                    valid = false;
                }
        } while (!valid);

        addToDo(toDos, name, description, dueDate);
        updateToDos(toDos);
    }

    public static void getTodaysToDos(List<ToDo> toDos) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime todaysDate = LocalDateTime.now();
        getToDosBy(toDos, todaysDate.format(dateFormat));
    }

    public static void addToDo(List<ToDo> ToDos, String name, String description, String dueDate) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("ToDos.json");

        try {
            ToDo newToDo = new ToDo(name, description, dueDate);
            ToDos.add(newToDo);
            objectMapper.writeValue(file, ToDos);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sortToDosByDueDate(List<ToDo> toDos) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        toDos.sort(new Comparator<ToDo>() {
            @Override
            public int compare(ToDo toDo1, ToDo toDo2) {
                try {
                    Date date1 = dateFormat.parse(toDo1.getDueDate());
                    Date date2 = dateFormat.parse(toDo2.getDueDate());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                    return 0;
                }
            }
        });
    }

    public static List<ToDo> getAllToDos() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ToDo> toDos = new ArrayList<>();
        try {
            toDos = objectMapper.readValue(new File("ToDos.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, ToDo.class));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return toDos;
    }

    public static ToDo getToDoById(List<ToDo> toDos, int id) {
        for (ToDo toDo : toDos) {
            if (toDo.getId() == id) {
                return toDo;
            }
        }
        return null;
    }

    public static List<ToDo> getToDosBy(List<ToDo> toDos, String constraint) {
        List<ToDo> filteredToDos = new ArrayList<>();

        constraint = constraint.toLowerCase();

        try {
            Integer.parseInt(constraint);
            for(ToDo toDo : toDos) {
                if (toDo.getId() == Integer.parseInt(constraint) | ((toDo.getDescription().toLowerCase().contains(constraint) | toDo.getName().toLowerCase().contains(constraint) | toDo.getDueDate().toLowerCase().contains(constraint)) && constraint.length() > 3)){
                    filteredToDos.add(toDo);
                }
            }
        } catch (NumberFormatException e) {
            for(ToDo toDo : toDos) {
                if ((toDo.getDescription().toLowerCase().contains(constraint) | toDo.getName().toLowerCase().contains(constraint) | toDo.getDueDate().toLowerCase().contains(constraint)) && constraint.length() > 5){
                    filteredToDos.add(toDo);
                }
            }
        }

        return filteredToDos;
    }

    public static void saveAllToDos(List<ToDo> toDos) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("ToDos.json"), toDos);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showMultToDos(List<ToDo> toDos) {
        for (ToDo toDo : toDos) {
            showToDo(toDo);
        }
    }

    public static void showToDo(ToDo toDo) {
        System.out.println(Line);
        if (!toDo.isCompleted()) {
            System.out.printf("| %-4s %-4s %10s \n", "⬜",toDo.getId(), toDo.getName());
        } else {
            System.out.printf("| %-4s %-4s %10s \n", "✅",toDo.getId(), toDo.getName());
        }
        System.out.println(Line);
        System.out.println("| " + toDo.getDescription());
        System.out.printf("| %-15s %22s \n", "Initial date", toDo.getInitialDate());
        System.out.printf("| %-15s %22s \n", "Due date", toDo.getDueDate());
        System.out.println(Line);
    }

    public static void toggleCompleted(ToDo toDo) {
        toDo.toggleCompleted();
    }

    public static String Line = "-----------------------------------------------------";
}
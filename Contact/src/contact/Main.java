package contact;

import java.io.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final String FILE_NAME = "Agenda";
    private static final int MAX_CONTACTS = 100;

    private static void create(String contact) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bufferedWriter.write(contact);
            bufferedWriter.newLine();
        } catch (IOException exception) {
            System.out.println("Erreur dans la fonction create : " + exception);
        }
    }

    private static String[] read() {
        String[] contacts = new String[MAX_CONTACTS];
        String line;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {
            short pos = 0;

            if (!new File(FILE_NAME).exists()) {
                create("");
            }

            while ((line = bufferedReader.readLine()) != null) {
                contacts[pos++] = line;
            }
        } catch (IOException exception) {
            System.out.println("Erreur dans la fonction read : " + exception);
        }

        return contacts;
    }

    private static void creer() {
        String[] contactFields = {"nom", "prénom", "numéro", "adresse", "email", "CIN"};
        StringBuilder contact = new StringBuilder();

        for (String field : contactFields) {
            System.out.print("Donner votre " + field + ": ");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            contact.append(input).append(";");
        }

        create(contact.toString().trim());
    }

    private static void lecture() {
        String[] contacts = read();
        String[] details;

        try {
            for (String str : contacts) {
                if (str != null && !str.isEmpty()) {
                    details = str.split(";");
                    System.out.println("Nom: " + details[0] + ", Prénom: " + details[1] + ", Numéro: " + details[2] +
                            ", Adresse: " + details[3] + ", Email: " + details[4] + ", CIN: " + details[5]);
                }
            }
        } catch (Exception ex) {
            System.out.println("Erreur dans la fonction lecture : \n" + ex);
        }
    }

    private static void reset() {
        String[] contacts = read();

        contacts = Arrays.stream(contacts)
                .filter(contact -> contact != null && !contact.trim().isEmpty())
                .toArray(String[]::new);

        Arrays.sort(contacts);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (String value : contacts) {
                if (value != null) {
                    bufferedWriter.write(value);
                    bufferedWriter.newLine();
                }
            }

            System.out.println("Mise à jour terminée avec succès.");

        } catch (IOException exception) {
            System.out.println("Erreur dans la fonction reset : " + exception);
        }
    }

    private static void supprimer() {
        String[] contacts = read();
        String suppr;
        short pos = -1;
        lecture();

        System.out.println("Indiquer le nom à supprimer : ");
        Scanner sc = new Scanner(System.in);
        suppr = sc.nextLine();

        try {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i] != null && contacts[i].contains(suppr)) {
                    contacts[i] = null;
                    pos = (short) i;
                    break;
                }
            }

            if (pos != -1) {
                remplir(contacts);
                System.out.println("Suppression terminée avec succès.");
            } else {
                System.out.println("Contact non trouvé.");
            }

        } catch (Exception ex) {
            System.out.println("Erreur " + ex);
        }
    }

    private static void remplir(String[] contacts) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (String value : contacts) {
                if (value != null) {
                    bufferedWriter.write(value);
                    bufferedWriter.newLine();
                }
            }
        } catch (IOException exception) {
            System.out.println("Erreur dans la fonction remplir : " + exception);
        }
    }

    private static void modifier() {
        String[] contacts = read();
        String modif;
        short pos = -1;
        lecture();

        System.out.println("Indiquer le nom à modifier : ");
        Scanner sc = new Scanner(System.in);
        modif = sc.nextLine();

        try {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i] != null && contacts[i].contains(modif)) {
                    pos = (short) i;
                    break;
                }
            }

            if (pos != -1) {
                System.out.println("Contact avant modification : " + contacts[pos]);

                String[] contactFields = {"nouveau nom", "nouveau prénom", "nouveau numéro", "nouvelle adresse", "nouvel email", "nouveau CIN"};
                StringBuilder nouveauContact = new StringBuilder();

                for (String field : contactFields) {
                    System.out.print("Donner le " + field + ": ");
                    String input = sc.nextLine();
                    nouveauContact.append(input).append(";");
                }

                contacts[pos] = nouveauContact.toString().trim();
                remplir(contacts);

                System.out.println("Modification terminée avec succès.");
            } else {
                System.out.println("Contact non trouvé.");
            }

        } catch (Exception ex) {
            System.out.println("Erreur " + ex);
        }
    }

    private static void table() {
        System.out.println("####### Menu Agenda Contact #######");
        System.out.print("  1. Créer contact \n  2. Lecture contact \n  3. Mise à jour \n  4. Effacer contact \n" +
                "  5. Modifier contact \n  6. Quitter \nVotre choix : ");
        short choix;
        Scanner sc = new Scanner(System.in);
        try {
            choix = sc.nextShort();
        } catch (InputMismatchException e) {
            System.out.println("Entrée invalide. Veuillez entrer un numéro.");
            sc.next();
            return;
        }

        switch (choix) {
            case 1 -> creer();
            case 2 -> lecture();
            case 3 -> reset();
            case 4 -> supprimer();
            case 5 -> modifier();
            case 6 -> System.exit(0);
            default -> System.out.println("Le choix doit être entre 1 et 6, mais vous avez tapé : " + choix);
        }
    }

    public static void main(String[] args) {
        while (true)
            table();
    }
}

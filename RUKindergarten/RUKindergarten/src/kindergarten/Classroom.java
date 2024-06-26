package kindergarten;

import javax.net.ssl.SNIMatcher;
import javax.sound.midi.ShortMessage;

/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student
 * in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given
 * seat), and
 * - a Student array parallel to seatingAvailability to show students filed into
 * seats
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in
 * studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine; // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs; // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability; // represents the classroom seats that are available to students
    private Student[][] studentsSitting; // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * 
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom(SNode l, SNode m, boolean[][] a, Student[][] s) {
        studentsInLine = l;
        musicalChairs = m;
        seatingAvailability = a;
        studentsSitting = s;
    }

    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in
     * line.
     * 
     * Reads students from input file and inserts these students in alphabetical
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the
     * file, say x
     * 2) x lines containing one student per line. Each line has the following
     * student
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom(String filename) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(filename); // read file
        int numStudent = StdIn.readInt(); // read the number of Students
        Student[] students = new Student[numStudent]; // makes array for the Students

        for (int i = 0; i < numStudent; i++) { // makes each student from file a Student object
            students[i] = new Student(StdIn.readString(), StdIn.readString(), StdIn.readInt());
        }

        // making alphabetical order
        Student temp;
        for (int i = 0; i < numStudent; i++) {
            for (int j = i + 1; j < numStudent; j++) {
                if (students[i].compareNameTo(students[j]) > 0) {
                    // swapping for alphabetical order
                    temp = students[i];
                    students[i] = students[j];
                    students[j] = temp;
                }
            }
        }

        // making a Linked List
        SNode head = new SNode(students[0], null);
        studentsInLine = head;
        SNode ptr = head;

        for (int i = 1; i < numStudent; i++) {
            ptr.setNext(new SNode(students[i], null));
            ptr = ptr.getNext();
        }

    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of
     * available seats inside the classroom. Imagine that unavailable seats are
     * broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an
     * available seat)
     * 
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(seatingChart);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        seatingAvailability = new boolean[r][c];
        studentsSitting = new Student[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                boolean sitting = StdIn.readBoolean();
                seatingAvailability[i][j] = sitting;
            }
        }

    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the
     * front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into
     * studentsSitting according to
     * seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents() {

        // WRITE YOUR CODE HERE
        if (musicalChairs != null) {
            for (int i = 0; i < seatingAvailability.length; i++) {
                for (int j = 0; j < seatingAvailability[i].length; j++) {
                    if (seatingAvailability[i][j] == true) {
                        studentsSitting[i][j] = musicalChairs.getStudent();
                        if (musicalChairs.getNext() == musicalChairs) {
                            musicalChairs = null;
                        }
                    }
                }
            }
        }
        if (musicalChairs == null) {
            for (int i = 0; i < seatingAvailability.length; i++) {
                for (int j = 0; j < seatingAvailability[i].length; j++) {
                    if (seatingAvailability[i][j] == true) {
                        if (studentsSitting[i][j] == null) {
                            studentsSitting[i][j] = studentsInLine.getStudent();
                            if (studentsInLine.getNext() == null) {
                                studentsInLine = null;
                                return;
                            } else {
                                studentsInLine = studentsInLine.getNext();
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then
     * moves
     * into second row.
     */

    public void insertMusicalChairs() {

        // WRITE YOUR CODE HERE
        SNode next = new SNode(null, null);
        for (int i = 0; i < studentsSitting.length; i++) {
            for (int j = 0; j < studentsSitting[i].length; j++) {
                if (studentsSitting[i][j] != null) {
                    if (musicalChairs == null) {
                        musicalChairs = new SNode(studentsSitting[i][j], null);
                        musicalChairs.setNext(musicalChairs);
                        studentsSitting[i][j] = null;
                    } else {
                        next = new SNode(studentsSitting[i][j], null);
                        next.setNext(musicalChairs.getNext());
                        musicalChairs.setNext(next);
                        musicalChairs = next;
                        studentsSitting[i][j] = null;
                    }
                }
            }
        }
    }

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is
     * only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using
     * StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first
     * student in the
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in
     * studentsInLine
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students
     * can be seated.
     */
    public void playMusicalChairs() {

        // WRITE YOUR CODE HERE
        if (musicalChairs == null) {
            return;
        }
        insertMusicalChairs();
        int chairLength = 1;
        SNode newNode = musicalChairs.getNext();
        do {
            newNode = newNode.getNext();
            chairLength++;
        } while (newNode != musicalChairs);
        do {
            Student s;
            int l = StdRandom.uniform(chairLength);
            chairLength--;
            SNode prev = musicalChairs, ptr = musicalChairs.getNext();

            if (l == 0) {
                newNode = ptr.getNext();
                musicalChairs.setNext(newNode);
                s = ptr.getStudent();
            }

            for (int i = 0; i < l; i++) {
                prev = ptr;
                ptr = ptr.getNext();
            }

            if (ptr == musicalChairs) {
                prev.setNext(musicalChairs.getNext());
                musicalChairs = prev;
            }

            prev.setNext(ptr.getNext());
            s = ptr.getStudent();
            SNode n = new SNode(s, null);

            if (studentsInLine == null) {
                studentsInLine = n;
            } else if (studentsInLine.getStudent().getHeight() >= n.getStudent().getHeight()) {
                newNode = studentsInLine;
                studentsInLine = n;
                studentsInLine.setNext(newNode);
            } else {
                prev = null;
                ptr = studentsInLine;
                while (ptr != null) {
                    if (ptr.getStudent().getHeight() < n.getStudent().getHeight()) {
                        prev = ptr;
                        ptr = ptr.getNext();
                        continue;
                    } else {
                        break;
                    }
                }
                if (prev != null) {
                    prev.setNext(n);
                    n.setNext(ptr);
                }

            }
        } while (chairLength > 1);

        Student dubs = null;

        if (musicalChairs == musicalChairs.getNext()) {
            dubs = musicalChairs.getStudent();
            musicalChairs = null;
        }

        boolean kingSeated = false;

        for (int i = 0; i < seatingAvailability.length; i++) {
            for (int j = 0; j < seatingAvailability[i].length; j++)
                if (seatingAvailability[i][j] == true) {
                    studentsSitting[i][j] = dubs;
                    seatingAvailability[i][j] = false;
                    kingSeated = true;
                    break;
                }

            if (kingSeated)
                break;
        }

        seatStudents();

    }

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is
     * not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * 
     * @param firstName the first name
     * @param lastName  the last name
     * @param height    the height of the student
     */
    public void addLateStudent(String firstName, String lastName, int height) {

        // WRITE YOUR CODE HERE

        // the late student to be added
        Student lateStudent = new Student(firstName, lastName, height);

        SNode temp = new SNode();
        temp.setStudent(lateStudent);

        if (studentsInLine == null) {
            if (musicalChairs == null) {
                // for studentsSitting
                for (int i = 0; i < studentsSitting.length; i++) {
                    for (int j = 0; j < studentsSitting[i].length; j++) {
                        if (seatingAvailability[i][j] == true && studentsSitting[i][j] == null) {
                            studentsSitting[i][j] = lateStudent;
                            return;
                        }
                    }
                }
            } else {
                // for musicalChairs
                temp.setNext(musicalChairs.getNext());
                musicalChairs.setNext(temp);
                musicalChairs = temp;
            }
        } else {
            // for studentsInLine
            SNode newnode = new SNode();
            newnode.setStudent(lateStudent);
            newnode.setNext(null);

            SNode last = studentsInLine;

            while (last.getNext() != null) {
                last = last.getNext();
            }
            last.setNext(newnode);
        }
    }

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName  the student's last name
     */
    public void deleteLeavingStudent(String firstName, String lastName) {

        // WRITE YOUR CODE HERE

    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine() {

        // Print studentsInLine
        StdOut.println("Students in Line:");
        if (studentsInLine == null) {
            StdOut.println("EMPTY");
        }

        for (SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext()) {
            StdOut.print(ptr.getStudent().print());
            if (ptr.getNext() != null) {
                StdOut.print(" -> ");
            }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents() {

        StdOut.println("Sitting Students:");

        if (studentsSitting != null) {

            for (int i = 0; i < studentsSitting.length; i++) {
                for (int j = 0; j < studentsSitting[i].length; j++) {

                    String stringToPrint = "";
                    if (studentsSitting[i][j] == null) {

                        if (seatingAvailability[i][j] == false) {
                            stringToPrint = "X";
                        } else {
                            stringToPrint = "EMPTY";
                        }

                    } else {
                        stringToPrint = studentsSitting[i][j].print();
                    }

                    StdOut.print(stringToPrint);

                    for (int o = 0; o < (10 - stringToPrint.length()); o++) {
                        StdOut.print(" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs() {
        StdOut.println("Students in Musical Chairs:");

        if (musicalChairs == null) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for (ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext()) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if (ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() {
        return studentsInLine;
    }

    public void setStudentsInLine(SNode l) {
        studentsInLine = l;
    }

    public SNode getMusicalChairs() {
        return musicalChairs;
    }

    public void setMusicalChairs(SNode m) {
        musicalChairs = m;
    }

    public boolean[][] getSeatingAvailability() {
        return seatingAvailability;
    }

    public void setSeatingAvailability(boolean[][] a) {
        seatingAvailability = a;
    }

    public Student[][] getStudentsSitting() {
        return studentsSitting;
    }

    public void setStudentsSitting(Student[][] s) {
        studentsSitting = s;
    }

}

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

// Interface for Account Operations
interface AccountOperations {
    void deposit(double amount);
    boolean withdraw(double amount);
    boolean transfer(double amount, Account targetAccount);
}

// Customer Class (Encapsulation)
class Customer {
    private final String id;
    private final String name;
    private final String pin;

    // Constructor to initialize customer details
    public Customer(String name, String pin) {
        this.id = generateId(); // Generate a unique ID
        this.name = name;
        this.pin = pin;
    }

    // Generate a unique 9-digit ID
    private String generateId() {
        Random random = new Random();
        return String.format("%09d", random.nextInt(1_000_000_000));
    }

    // Getters for private attributes
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPin() {
        return pin;
    }
}

// Abstract Account Class (Inheritance)
abstract class Account implements AccountOperations {
    private final Customer customer;
    private double balance;
    private final List<String> transactionHistory;
    private final double interestRate; // Interest rate for the account

    // Constructor to initialize account details
    public Account(Customer customer, double initialDeposit, double interestRate) {
        this.customer = customer;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        this.interestRate = interestRate;
        recordTransaction("Initial deposit of \u20B1" + initialDeposit); // Record initial deposit
    }

    // Getters for private attributes
    public Customer getCustomer() {
        return customer;
    }
    public double getBalance() {
        return balance;
    }
    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
    public double getInterestRate() {
        return interestRate;
    }

    // Calculate monthly interest based on the balance and interest rate
    public double calculateMonthlyInterest() {
        return balance * interestRate / 100;
    }

    // Deposit funds into the account
    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            recordTransaction("Deposited \u20B1" + amount);
        }
    }

    // Withdraw funds from the account if sufficient balance is available
    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            recordTransaction("Withdrew \u20B1" + amount);
            return true;
        }
        return false;
    }

    // Transfer funds to another account
    @Override
    public boolean transfer(double amount, Account targetAccount) {
        if (amount > 0 && amount <= balance) {
            this.withdraw(amount);
            targetAccount.deposit(amount);
            // Record transaction details for sender and receiver
            recordTransaction("Transferred ₱" + amount + " to " +
                    targetAccount.getCustomer().getName() +
                    " (ID: " + targetAccount.getCustomer().getId() + ")");
            targetAccount.recordTransaction("Received ₱" + amount + " from " +
                    this.getCustomer().getName() +
                    " (ID: " + this.getCustomer().getId() + ")");
            return true;
        }
        return false;
    }

    // Record transactions in the transaction history
    private void recordTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    // Abstract method to define account type
    public abstract String getAccountType();
}

// SavingsAccount Class
class SavingsAccount extends Account {
    public SavingsAccount(Customer customer, double initialDeposit) {
        super(customer, initialDeposit, 5.0); // 5% interest for savings
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }
}

// CheckingAccount Class
class CheckingAccount extends Account {
    public CheckingAccount(Customer customer, double initialDeposit) {
        super(customer, initialDeposit, 3.0); // 3% interest for checking
    }

    @Override
    public String getAccountType() {
        return "Checking Account";
    }
}

// Main class for the Banking System application
public class BankingSystem {
    private static ArrayList<Account> accounts = new ArrayList<>(); // Store all accounts
    private static Account currentAccount; // Store the currently logged-in account

    public static void main(String[] args) {
        // Setup JFrame and main layout
        JFrame frame = new JFrame("Bank System");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CardLayout cardLayout = new CardLayout(); // Card layout for switching between panels
        frame.setLayout(cardLayout);

        // Panels for different screens
        JPanel mainMenuPanel = new JPanel();
        JPanel signUpPanel = new JPanel();
        JPanel loginPanel = new JPanel();
        JPanel accountPanel = new JPanel();
        JPanel transactionPanel = new JPanel();

        // Configure panel layouts
        mainMenuPanel.setLayout(new GridLayout(3, 1));
        signUpPanel.setLayout(new GridLayout(6, 2));
        loginPanel.setLayout(new GridLayout(4, 2));
        accountPanel.setLayout(new GridLayout(6, 1));
        transactionPanel.setLayout(new GridLayout(5, 1));

        // Main menu buttons
        JButton signUpButton = new JButton("Sign Up");
        JButton loginButton = new JButton("Log In");
        JButton exitButton = new JButton("Exit");
        mainMenuPanel.add(signUpButton);
        mainMenuPanel.add(loginButton);
        mainMenuPanel.add(exitButton);

        // Sign-up components
        JTextField nameField = new JTextField();
        JPasswordField pinField = new JPasswordField();
        JComboBox<String> accountTypeBox = new JComboBox<>(new String[]{"Savings Account", "Checking Account"});
        JTextField depositField = new JTextField();
        JButton createAccountButton = new JButton("Create Account");
        JButton backToMainMenuButton1 = new JButton("Back to Main Menu");

        signUpPanel.add(new JLabel("Name:"));
        signUpPanel.add(nameField);
        signUpPanel.add(new JLabel("PIN (4 digits):"));
        signUpPanel.add(pinField);
        signUpPanel.add(new JLabel("Account Type:"));
        signUpPanel.add(accountTypeBox);
        signUpPanel.add(new JLabel("Initial Deposit (min ₱100):"));
        signUpPanel.add(depositField);
        signUpPanel.add(createAccountButton);
        signUpPanel.add(backToMainMenuButton1);

        // Create account logic
        createAccountButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String pin = new String(pinField.getPassword());
                double initialDeposit = Double.parseDouble(depositField.getText());

                if (initialDeposit < 100) {
                    JOptionPane.showMessageDialog(frame, "Initial deposit must be at least ₱100.");
                    return;
                }

                String accountType = (String) accountTypeBox.getSelectedItem();
                Customer newCustomer = new Customer(name, pin);
                Account newAccount;

                if (accountType.equals("Savings Account")) {
                    newAccount = new SavingsAccount(newCustomer, initialDeposit);
                } else {
                    newAccount = new CheckingAccount(newCustomer, initialDeposit);
                }

                accounts.add(newAccount);
                JOptionPane.showMessageDialog(frame, "Account Created!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid deposit amount.");
            }
        });

        // Back to main menu logic
        backToMainMenuButton1.addActionListener(e -> cardLayout.show(frame.getContentPane(), "mainMenuPanel"));

        // Login components
        JTextField loginPinField = new JTextField();
        JButton loginSubmitButton = new JButton("Log In");
        JButton backToMainMenuButton2 = new JButton("Back to Main Menu");

        loginPanel.add(new JLabel("Enter PIN:"));
        loginPanel.add(loginPinField);
        loginPanel.add(loginSubmitButton);
        loginPanel.add(backToMainMenuButton2);

        // Login logic
        loginSubmitButton.addActionListener(e -> {
            String enteredPin = loginPinField.getText();
            for (Account account : accounts) {
                if (account.getCustomer().getPin().equals(enteredPin)) {
                    currentAccount = account;
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    cardLayout.show(frame.getContentPane(), "accountPanel");
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "Incorrect PIN. Please try again.");
        });

        // Back to main menu logic for login
        backToMainMenuButton2.addActionListener(e -> cardLayout.show(frame.getContentPane(), "mainMenuPanel"));

        // Account panel components
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");
        JButton logoutButton = new JButton("Log Out");
        JButton viewTransactionHistoryButton = new JButton("View Transaction History");

        accountPanel.add(new JLabel("Welcome, " + currentAccount.getCustomer().getName()));
        accountPanel.add(depositButton);
        accountPanel.add(withdrawButton);
        accountPanel.add(transferButton);
        accountPanel.add(viewTransactionHistoryButton);
        accountPanel.add(logoutButton);

        // Deposit logic
        depositButton.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog("Enter deposit amount:");
            double amount = Double.parseDouble(amountStr);
            currentAccount.deposit(amount);
            JOptionPane.showMessageDialog(frame, "Deposit successful.");
        });

        // Withdraw logic
        withdrawButton.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog("Enter withdrawal amount:");
            double amount = Double.parseDouble(amountStr);
            if (currentAccount.withdraw(amount)) {
                JOptionPane.showMessageDialog(frame, "Withdrawal successful.");
            } else {
                JOptionPane.showMessageDialog(frame, "Insufficient balance.");
            }
        });

        // Transfer logic
        transferButton.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog("Enter transfer amount:");
            double amount = Double.parseDouble(amountStr);
            String recipientId = JOptionPane.showInputDialog("Enter recipient ID:");
            Account targetAccount = null;
            for (Account account : accounts) {
                if (account.getCustomer().getId().equals(recipientId)) {
                    targetAccount = account;
                    break;
                }
            }
            if (targetAccount != null && currentAccount.transfer(amount, targetAccount)) {
                JOptionPane.showMessageDialog(frame, "Transfer successful.");
            } else {
                JOptionPane.showMessageDialog(frame, "Transfer failed.");
            }
        });

        // View transaction history
        viewTransactionHistoryButton.addActionListener(e -> {
            StringBuilder history = new StringBuilder("Transaction History:\n");
            for (String transaction : currentAccount.getTransactionHistory()) {
                history.append(transaction).append("\n");
            }
            JOptionPane.showMessageDialog(frame, history.toString());
        });

        // Log out logic
        logoutButton.addActionListener(e -> {
            currentAccount = null;
            JOptionPane.showMessageDialog(frame, "Logged out successfully.");
            cardLayout.show(frame.getContentPane(), "mainMenuPanel");
        });

        // Setup initial display
        frame.setVisible(true);
        cardLayout.show(frame.getContentPane(), "mainMenuPanel");
    }
}

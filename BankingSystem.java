import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

// Customer Class (Encapsulation)
class Customer {
    private final String id;
    private final String name;
    private final String pin;

    public Customer(String name, String pin) {
        this.id = generateId();
        this.name = name;
        this.pin = pin;
    }

    private String generateId() {
        Random random = new Random();
        return String.format("%09d", random.nextInt(1_000_000_000));
    }

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
abstract class Account {
    private final Customer customer;
    private double balance;
    private final List<String> transactionHistory;
    private final double interestRate;  // Added interest rate

    public Account(Customer customer, double initialDeposit, double interestRate) {
        this.customer = customer;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        this.interestRate = interestRate;  // Set the interest rate
        recordTransaction("Initial deposit of ₱" + initialDeposit);
    }

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

    // Method to calculate the interest for the next month
    public double calculateMonthlyInterest() {
        return balance * interestRate / 100;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            recordTransaction("Deposited ₱" + amount);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            recordTransaction("Withdrew ₱" + amount);
            return true;
        }
        return false;
    }

    public boolean transfer(double amount, Account targetAccount) {
        if (amount > 0 && amount <= balance) {
            this.withdraw(amount);
            targetAccount.deposit(amount);
            recordTransaction("Transferred ₱" + amount + " to Account ID: " + targetAccount.getCustomer().getId());
            targetAccount.recordTransaction("Received ₱" + amount + " from Account ID: " + this.getCustomer().getId());
            return true;
        }
        return false;
    }

    private void recordTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

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


public class BankingSystem {
    private static ArrayList<Account> accounts = new ArrayList<>();
    private static Account currentAccount;

    public static void main(String[] args) {
        // Main frame setup
        JFrame frame = new JFrame("Bank System");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set explicit CardLayout for frame
        CardLayout cardLayout = new CardLayout();
        frame.setLayout(cardLayout);

        // Panels
        JPanel mainMenuPanel = new JPanel();
        JPanel signUpPanel = new JPanel();
        JPanel loginPanel = new JPanel();
        JPanel accountPanel = new JPanel();
        JPanel transactionPanel = new JPanel();

        // Layouts
        mainMenuPanel.setLayout(new GridLayout(3, 1));
        signUpPanel.setLayout(new GridLayout(6, 2));
        loginPanel.setLayout(new GridLayout(4, 2));
        accountPanel.setLayout(new GridLayout(6, 1));
        transactionPanel.setLayout(new GridLayout(5, 1));

        // Main Menu Components
        JButton signUpButton = new JButton("Sign Up");
        JButton loginButton = new JButton("Log In");
        JButton exitButton = new JButton("Exit");

        mainMenuPanel.add(signUpButton);
        mainMenuPanel.add(loginButton);
        mainMenuPanel.add(exitButton);

        // Sign-Up Components
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

        // Login Components
        JTextField idField = new JTextField();
        JPasswordField loginPinField = new JPasswordField();
        JButton loginSubmitButton = new JButton("Log In");
        JButton backToMainMenuButton2 = new JButton("Back to Main Menu");

        loginPanel.add(new JLabel("ID:"));
        loginPanel.add(idField);
        loginPanel.add(new JLabel("PIN:"));
        loginPanel.add(loginPinField);
        loginPanel.add(loginSubmitButton);
        loginPanel.add(backToMainMenuButton2);

        // Account Panel Components
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");
        JButton viewInfoButton = new JButton("View Info");
        JButton viewHistoryButton = new JButton("View Transaction History");
        JButton logoutButton = new JButton("Log Out");

        accountPanel.add(depositButton);
        accountPanel.add(withdrawButton);
        accountPanel.add(transferButton);
        accountPanel.add(viewInfoButton);
        accountPanel.add(viewHistoryButton);
        accountPanel.add(logoutButton);

        // Transaction Panel Components
        JTextField targetAccountField = new JTextField();
        JTextField amountField = new JTextField();
        JButton transactionSubmitButton = new JButton("Submit");
        JButton backToAccountButton = new JButton("Back to Account");

        transactionPanel.add(new JLabel("Target Account ID (for transfers):"));
        transactionPanel.add(targetAccountField);
        transactionPanel.add(new JLabel("Amount:"));
        transactionPanel.add(amountField);
        transactionPanel.add(transactionSubmitButton);
        transactionPanel.add(backToAccountButton);

        // Add panels to frame
        frame.add(mainMenuPanel, "MainMenu");
        frame.add(signUpPanel, "SignUp");
        frame.add(loginPanel, "Login");
        frame.add(accountPanel, "Account");
        frame.add(transactionPanel, "Transaction");

        // Button Actions
        signUpButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "SignUp"));

        loginButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Login"));

        exitButton.addActionListener(e -> System.exit(0));

        backToMainMenuButton1.addActionListener(e -> cardLayout.show(frame.getContentPane(), "MainMenu"));

        backToMainMenuButton2.addActionListener(e -> cardLayout.show(frame.getContentPane(), "MainMenu"));

        backToAccountButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Account"));

        createAccountButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();
            String accountType = (String) accountTypeBox.getSelectedItem();
            String depositInput = depositField.getText().trim();
        
            if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(frame, "Invalid name. Please enter alphabetic characters only.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            if (!pin.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(frame, "Invalid PIN. Must be exactly 4 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            double deposit;
            try {
                deposit = Double.parseDouble(depositInput);
                if (deposit < 100) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid deposit amount. Must be at least ₱100.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            Customer customer = new Customer(name, pin);
            Account account;
            if ("Savings Account".equals(accountType)) {
                account = new SavingsAccount(customer, deposit);
            } else {
                account = new CheckingAccount(customer, deposit);
            }
            accounts.add(account);
        
            // Copy the ID to clipboard
            String accountId = customer.getId();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(accountId), null);
        
            JOptionPane.showMessageDialog(frame, "Account created successfully!\nYour ID: " + accountId + "\n(ID copied to clipboard)", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear the text fields
            nameField.setText("");
            pinField.setText("");
            depositField.setText("");
            accountTypeBox.setSelectedIndex(0);  // Resets the combo box to the default option (Savings Account)
        
            cardLayout.show(frame.getContentPane(), "MainMenu");
        });
        
        

        loginSubmitButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String pin = new String(loginPinField.getPassword()).trim();

            for (Account account : accounts) {
                if (account.getCustomer().getId().equals(id) && account.getCustomer().getPin().equals(pin)) {
                    currentAccount = account;
                    JOptionPane.showMessageDialog(frame, "Welcome " + currentAccount.getCustomer().getName() + "!");
                    cardLayout.show(frame.getContentPane(), "Account");
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "Invalid ID or PIN.", "Error", JOptionPane.ERROR_MESSAGE);
        });

        logoutButton.addActionListener(e -> {
            currentAccount = null;
            cardLayout.show(frame.getContentPane(), "MainMenu");
        });

        depositButton.addActionListener(e -> {
            String amountInput = JOptionPane.showInputDialog(frame, "Enter amount to deposit:", "Deposit", JOptionPane.PLAIN_MESSAGE);
            if (amountInput != null) {
                try {
                    double amount = Double.parseDouble(amountInput);
                    if (amount < 100) {
                        JOptionPane.showMessageDialog(frame, "The minimum deposit is ₱100. Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (amount > 0) {
                        currentAccount.deposit(amount);
                        JOptionPane.showMessageDialog(frame, "Deposited ₱" + amount + " successfully!", "Deposit", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Amount must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        

        withdrawButton.addActionListener(e -> {
            String amountInput = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:", "Withdraw", JOptionPane.PLAIN_MESSAGE);
            if (amountInput != null) {
                try {
                    double amount = Double.parseDouble(amountInput);
        
                    // Check if the amount is within the specified range
                    if (amount < 100) {
                        JOptionPane.showMessageDialog(frame, "The minimum withdrawal is ₱100. Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (amount > 10000) {
                        JOptionPane.showMessageDialog(frame, "The maximum withdrawal is ₱10,000. Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
        
                    // Check if there are sufficient funds for the withdrawal
                    if (amount > 0 && currentAccount.withdraw(amount)) {
                        JOptionPane.showMessageDialog(frame, "Withdrew ₱" + amount + " successfully!", "Withdraw", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Insufficient funds or invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        transferButton.addActionListener(e -> {
            transactionSubmitButton.setText("Transfer");
            targetAccountField.setEnabled(true);
            cardLayout.show(frame.getContentPane(), "Transaction");
        });
        
        transactionSubmitButton.addActionListener(e -> {
            String buttonText = transactionSubmitButton.getText();
            String targetAccountId = targetAccountField.getText().trim();
            String amountInput = amountField.getText().trim();
        
            if (buttonText.equals("Transfer")) {
                try {
                    double amount = Double.parseDouble(amountInput);
        
                    // Check if the amount is within the specified range for transfers
                    if (amount < 100) {
                        JOptionPane.showMessageDialog(frame, "The minimum transfer amount is ₱100. Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (amount > 100000) {
                        JOptionPane.showMessageDialog(frame, "The maximum transfer amount is ₱100,000. Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
        
                    // Find the target account by ID
                    Account targetAccount = null;
                    for (Account account : accounts) {
                        if (account.getCustomer().getId().equals(targetAccountId)) {
                            targetAccount = account;
                            break;
                        }
                    }
        
                    if (targetAccount == null) {
                        JOptionPane.showMessageDialog(frame, "Target account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
        
                    // Check if the transfer is successful
                    if (currentAccount.transfer(amount, targetAccount)) {
                        JOptionPane.showMessageDialog(frame, "Transferred ₱" + amount + " to Account ID: " + targetAccountId, "Transfer Successful", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Insufficient funds or invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        
            // Reset fields and return to Account panel
            targetAccountField.setText("");
            amountField.setText("");
            cardLayout.show(frame.getContentPane(), "Account");
        });
        
        
        viewInfoButton.addActionListener(e -> {
            if (currentAccount != null) {
                double monthlyInterest = currentAccount.calculateMonthlyInterest();
                String info = "Account Type: " + currentAccount.getAccountType() +
                              "\nBalance: ₱" + currentAccount.getBalance() +
                              "\nName: " + currentAccount.getCustomer().getName() +
                              "\nID: " + currentAccount.getCustomer().getId() +
                              "\nInterest Rate: " + currentAccount.getInterestRate() + "%" +
                              "\nInterest for Next Month: ₱" + String.format("%.2f", monthlyInterest);
                JOptionPane.showMessageDialog(frame, info, "Account Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        viewHistoryButton.addActionListener(e -> {
            if (currentAccount != null) {
                StringBuilder history = new StringBuilder("Transaction History:\n");
                for (String transaction : currentAccount.getTransactionHistory()) {
                    history.append(transaction).append("\n");
                }
                JOptionPane.showMessageDialog(frame, history.toString(), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Final frame setup
        frame.setVisible(true);
    }
}

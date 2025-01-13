package com.example.expenseapi;

import com.example.expenseapi.pojo.*;
import com.example.expenseapi.repository.*;
import com.example.expenseapi.utils.CurrencyRatesFetcher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ExpenseApiApplication implements CommandLineRunner {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final ArchivedGroupRepository archivedGroupRepository;
    private final CurrencyRepository currencyRepository;
    private final PasswordEncoder passwordEncoder;
    private final MethodOfPaymentRepository methodOfPaymentRepository;
    private final RoleRepository roleRepository;
    private final PreferenceRepository preferenceRepository;

    public ExpenseApiApplication(
            ExpenseRepository expenseRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            GroupRepository groupRepository,
            MembershipRepository membershipRepository,
            ArchivedGroupRepository archivedGroupRepository,
            CurrencyRepository currencyRepository,
            PasswordEncoder passwordEncoder,
            MethodOfPaymentRepository methodOfPaymentRepository,
            RoleRepository roleRepository,
            PreferenceRepository preferenceRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.archivedGroupRepository = archivedGroupRepository;
        this.currencyRepository = currencyRepository;
        this.passwordEncoder = passwordEncoder;
        this.methodOfPaymentRepository = methodOfPaymentRepository;
        this.roleRepository = roleRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExpenseApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        initMethodOfPayments();
        initCurrencies();
        initGroups();
        initArchivedGroups();
        initPreferences();
        initUsers();
        initRoles();
        initMemberships();
        initCategories();
        initExpenses();
    }

    private void initMethodOfPayments() {
        if (methodOfPaymentRepository.count() == 0) {
            List<MethodOfPayment> methods = Arrays.asList(
                    new MethodOfPayment("cash"),
                    new MethodOfPayment("debit card"),
                    new MethodOfPayment("crypto"),
                    new MethodOfPayment("bank transfer"),
                    new MethodOfPayment("credit card"),
                    new MethodOfPayment("mobile payment")
            );
            methodOfPaymentRepository.saveAll(methods);
        }
    }

    private void initCurrencies() {
        if (currencyRepository.count() == 0) {
            List<Currency> currencies = Arrays.asList(
                    new Currency("PLN", 1.0),
                    new Currency("USD", CurrencyRatesFetcher.getCurrencyRates("USD")),
                    new Currency("EUR", CurrencyRatesFetcher.getCurrencyRates("EUR")),
                    new Currency("GBP", CurrencyRatesFetcher.getCurrencyRates("GBP")),
                    new Currency("CHF", CurrencyRatesFetcher.getCurrencyRates("CHF"))
            );
            currencyRepository.saveAll(currencies);
        }
    }

    private void initGroups() {
        if (groupRepository.count() == 0) {
            List<Group> groups = Arrays.asList(
                    new Group("family"),
                    new Group("workers"),
                    new Group("friends"),
                    new Group("sports club"),
                    new Group("volunteers")
            );
            groupRepository.saveAll(groups);
        }
    }

    private void initArchivedGroups() {
        if (archivedGroupRepository.count() == 0) {
            List<ArchivedGroup> archivedGroups = Arrays.asList(
                    new ArchivedGroup("family2"),
                    new ArchivedGroup("workers2"),
                    new ArchivedGroup("old friends"),
                    new ArchivedGroup("previous volunteers")
            );
            archivedGroupRepository.saveAll(archivedGroups);
        }
    }

    private void initPreferences() {
        if (preferenceRepository.count() == 0) {
            Currency pln = currencyRepository.findBySymbol("PLN").orElseThrow();
            Currency usd = currencyRepository.findBySymbol("USD").orElseThrow();
            Currency eur = currencyRepository.findBySymbol("EUR").orElseThrow();
            Currency gbp = currencyRepository.findBySymbol("GBP").orElseThrow();
            Currency chf = currencyRepository.findBySymbol("CHF").orElseThrow();

            MethodOfPayment cash = methodOfPaymentRepository.findByName("cash").orElseThrow();
            MethodOfPayment card = methodOfPaymentRepository.findByName("debit card").orElseThrow();
            MethodOfPayment crypto = methodOfPaymentRepository.findByName("crypto").orElseThrow();
            MethodOfPayment transfer = methodOfPaymentRepository.findByName("bank transfer").orElseThrow();

            List<Preference> preferences = Arrays.asList(
                    new Preference(pln, cash),
                    new Preference(usd, card),
                    new Preference(eur, crypto),
                    new Preference(gbp, transfer),
                    new Preference(chf, cash),
                    new Preference(chf, transfer),
                    new Preference(pln, crypto),
                    new Preference(eur, cash)
            );
            preferenceRepository.saveAll(preferences);
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            List<Preference> allPreferences = preferenceRepository.findAll();
            if (allPreferences.size() < 8) {
                return;
            }
            List<User> users = Arrays.asList(
                    new User("Jurek", "Ogórek", "jurek.ogorek@gmail.com", allPreferences.get(0), passwordEncoder.encode("123")),
                    new User("Jan", "Nowak", "jan.nowak@gmail.com", allPreferences.get(1), passwordEncoder.encode("234")),
                    new User("Franek", "Ganek", "franek.ganek@gmail.com", allPreferences.get(2), passwordEncoder.encode("345")),
                    new User("Pola", "Zopola", "pola.zopola@gmail.com", allPreferences.get(3), passwordEncoder.encode("456")),
                    new User("Stanisław", "Krasnystaw", "stanislaw.krasnystaw@gmail.com", allPreferences.get(4), passwordEncoder.encode("567")),
                    new User("Anna", "Nowakowska", "anna.nowakowska@gmail.com", allPreferences.get(5), passwordEncoder.encode("678")),
                    new User("Maria", "Wiśniewska", "maria.wisniewska@gmail.com", allPreferences.get(6), passwordEncoder.encode("789")),
                    new User("Piotr", "Zieliński", "piotr.zielinski@gmail.com", allPreferences.get(7), passwordEncoder.encode("890"))
            );
            userRepository.saveAll(users);
        }
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            List<Role> roles = Arrays.asList(
                    new Role("admin"),
                    new Role("member")
            );
            roleRepository.saveAll(roles);
        }
    }

    private void initMemberships() {
        if (membershipRepository.count() == 0) {
            List<User> users = userRepository.findAll();
            List<Group> groups = groupRepository.findAll();
            List<Role> roles = roleRepository.findAll();
            if (users.size() < 5 || groups.size() < 3 || roles.size() < 2) {
                return;
            }
            List<Membership> memberships = Arrays.asList(
                    new Membership(users.get(0), groups.get(0), roles.get(0)),
                    new Membership(users.get(1), groups.get(0), roles.get(1)),
                    new Membership(users.get(2), groups.get(1), roles.get(1)),
                    new Membership(users.get(3), groups.get(2), roles.get(0)),
                    new Membership(users.get(4), groups.get(2), roles.get(1)),
                    new Membership(users.get(5), groups.get(3), roles.get(0)),
                    new Membership(users.get(6), groups.get(4), roles.get(1))
            );
            membershipRepository.saveAll(memberships);
        }
    }

    private void initCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    new Category("food"),
                    new Category("transport"),
                    new Category("business"),
                    new Category("health"),
                    new Category("education"),
                    new Category("gym"),
                    new Category("furniture"),
                    new Category("electronics"),
                    new Category("leisure"),
                    new Category("other")
            );
            categoryRepository.saveAll(categories);
        }
    }

    private void initExpenses() {
        if (expenseRepository.count() == 0) {
            List<Membership> memberships = membershipRepository.findAll();
            List<Category> categories = categoryRepository.findAll();
            List<Currency> currencies = currencyRepository.findAll();
            List<MethodOfPayment> methods = methodOfPaymentRepository.findAll();

            if (memberships.size() < 5 || categories.isEmpty() || currencies.isEmpty() || methods.isEmpty()) {
                return;
            }

            List<Expense> expenses = Arrays.asList(
                    new Expense("lunch", 50, memberships.get(0), categories.get(0), LocalDate.now(), currencies.get(0), methods.get(0)),
                    new Expense("dinner", 120, memberships.get(1), categories.get(0), LocalDate.now().minusDays(2), currencies.get(1), methods.get(1)),
                    new Expense("train-ticket", 200, memberships.get(2), categories.get(1), LocalDate.now().minusDays(10), currencies.get(1), methods.get(1)),
                    new Expense("groceries", 300, memberships.get(3), categories.get(0), LocalDate.now().minusWeeks(1), currencies.get(2), methods.get(2)),
                    new Expense("fast-food", 45, memberships.get(4), categories.get(0), LocalDate.now().minusMonths(1), currencies.get(3), methods.get(3)),
                    new Expense("laptop", 3000, memberships.get(5), categories.get(8), LocalDate.now().minusDays(5), currencies.get(1), methods.get(4)),
                    new Expense("gym membership", 150, memberships.get(6), categories.get(5), LocalDate.now().minusDays(3), currencies.get(0), methods.get(5))
            );

            expenseRepository.saveAll(expenses);
        }
    }

}

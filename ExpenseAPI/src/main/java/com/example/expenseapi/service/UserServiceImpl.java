package com.example.expenseapi.service;

import com.example.expenseapi.dto.ChangePasswordDTO;
import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.dto.UserUpdateDTO;
import com.example.expenseapi.exception.BadRequestException;
import com.example.expenseapi.exception.ForbiddenRequestException;
import com.example.expenseapi.filter.UserFilter;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.Preference;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.*;
import com.example.expenseapi.specification.UserSpecification;
import com.example.expenseapi.utils.AuthHelper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CurrencyRepository currencyRepository;
    private final MethodOfPaymentRepository methodOfPaymentRepository;
    private final PreferenceRepository preferenceRepository;
    private final ExpenseService expenseService;
    private final MembershipService membershipService;
    private final TemporaryMembershipService temporaryMembershipService;
    private final RefreshTokenService refreshTokenService;

    public UserServiceImpl(UserRepository repository, UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, CurrencyRepository currencyRepository, MethodOfPaymentRepository methodOfPaymentRepository, PreferenceRepository preferenceRepository, ExpenseService expenseService, MembershipService membershipService, TemporaryMembershipService temporaryMembershipService, RefreshTokenService refreshTokenService) {
        super(repository);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.currencyRepository = currencyRepository;
        this.methodOfPaymentRepository = methodOfPaymentRepository;
        this.preferenceRepository = preferenceRepository;
        this.expenseService = expenseService;
        this.membershipService = membershipService;
        this.temporaryMembershipService = temporaryMembershipService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDTO> searchUsersDTO(UserFilter filter, String groupName) {
        if (AuthHelper.isGroupNameInvalid(groupName))
            throw new ForbiddenRequestException("User is not in the group");
        Specification<User> spec = Specification.where(null);
        spec = spec.and(UserSpecification.nameContains(filter.getName()));
        spec = spec.and(UserSpecification.surnameContains(filter.getSurname()));
        spec = spec.and(UserSpecification.notInGroup(groupName));
        return userRepository.findAll(spec).stream()
                .map(userMapper::userToUserDTO)
                .toList();

    }

    @Override
    public UserDTO changePassword(ChangePasswordDTO passwordDTO) {
        Optional<User> user = findByEmail(AuthHelper.getUserEmail());
        UserDTO response = null;
        if (user.isPresent()) {
            User temp = user.get();
            temp.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            userRepository.save(temp);
            response = userMapper.userToUserDTO(temp);
        }
        return response;
    }

    @Override
    public UserDTO update(UserUpdateDTO userUpdateDTO) {
        Optional<User> user = findByEmail(AuthHelper.getUserEmail());
        UserDTO response = null;
        if (user.isPresent()) {
            User temp = user.get();
            if (userUpdateDTO.getEmail() != null) {
                if (userRepository.findByEmail(userUpdateDTO.getEmail()).isPresent() &&
                    !temp.getEmail().equals(userUpdateDTO.getEmail()))
                    throw new BadRequestException("Email already exists");
                temp.setEmail(userUpdateDTO.getEmail());
            }
            if (userUpdateDTO.getName() != null){
                temp.setName(userUpdateDTO.getName());
            }
            if (userUpdateDTO.getSurname() != null){
                temp.setSurname(userUpdateDTO.getSurname());
            }
            response = userMapper.userToUserDTO(userRepository.save(temp));
        }
        return response;
    }

    @Override
    public User save(User entity) {
        if (entity.getPreference() == null) {
            entity.setPreference(preferenceRepository.save(new Preference(
                    currencyRepository.findById(1L).get(),
                    methodOfPaymentRepository.findById(1L).get()
            )));
        }
        return super.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        expenseService.deleteAllExpensesForUserId(id);
        membershipService.deleteAllMembershipsForUserId(id);
        temporaryMembershipService.deleteAllTemporaryMembershipsForUser(id);
        refreshTokenService.deleteByUserId(id);
        super.delete(id);
    }
}


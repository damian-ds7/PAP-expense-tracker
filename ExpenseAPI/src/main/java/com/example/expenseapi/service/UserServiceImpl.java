package com.example.expenseapi.service;

import com.example.expenseapi.dto.ChangePasswordDTO;
import com.example.expenseapi.dto.UserDTO;
import com.example.expenseapi.dto.UserUpdateDTO;
import com.example.expenseapi.filter.UserFilter;
import com.example.expenseapi.mapper.UserMapper;
import com.example.expenseapi.pojo.Preference;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.CurrencyRepository;
import com.example.expenseapi.repository.MethodOfPaymentRepository;
import com.example.expenseapi.repository.PreferenceRepository;
import com.example.expenseapi.repository.UserRepository;
import com.example.expenseapi.specification.UserSpecification;
import com.example.expenseapi.utils.AuthHelper;
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

    public UserServiceImpl(UserRepository repository, UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, CurrencyRepository currencyRepository, MethodOfPaymentRepository methodOfPaymentRepository, PreferenceRepository preferenceRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.currencyRepository = currencyRepository;
        this.methodOfPaymentRepository = methodOfPaymentRepository;
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDTO> searchUsersDTO(UserFilter filter, String groupName) {
        Specification<User> spec = Specification.where(null);
        spec = spec.and(UserSpecification.nameEquals(filter.getName()));
        spec = spec.and(UserSpecification.surnameEquals(filter.getSurname()));
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
            if (userUpdateDTO.getEmail() != null){
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
}


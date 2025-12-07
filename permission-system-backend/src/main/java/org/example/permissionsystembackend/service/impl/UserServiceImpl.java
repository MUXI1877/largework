package org.example.permissionsystembackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.permissionsystembackend.common.BusinessException;
import org.example.permissionsystembackend.common.ResultCode;
import org.example.permissionsystembackend.dto.LoginDTO;
import org.example.permissionsystembackend.dto.UserRegisterDTO;
import org.example.permissionsystembackend.entity.Option;
import org.example.permissionsystembackend.entity.Role;
import org.example.permissionsystembackend.entity.User;
import org.example.permissionsystembackend.repository.OptionRepository;
import org.example.permissionsystembackend.repository.RoleRepository;
import org.example.permissionsystembackend.repository.UserRepository;
import org.example.permissionsystembackend.service.UserService;
import org.example.permissionsystembackend.util.JwtUtil;
import org.example.permissionsystembackend.vo.LoginVO;
import org.example.permissionsystembackend.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OptionRepository optionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public Map<String, Object> checkIdCard(String idCard) {
        Map<String, Object> result = new HashMap<>();
        User user = userRepository.findByIdCard(idCard).orElse(null);

        if (user != null) {
            result.put("registered", true);
            result.put("username", user.getUsername());
            result.put("name", user.getName());
        } else {
            result.put("registered", false);
        }

        return result;
    }

    @Override
    @Transactional
    public void register(UserRegisterDTO dto) {
        // 验证身份证号和手机号是否已存在
        if (userRepository.existsByIdCard(dto.getIdCard())) {
            throw new BusinessException(ResultCode.IDCARD_ALREADY_REGISTERED);
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new BusinessException(ResultCode.PHONE_ALREADY_REGISTERED);
        }

        User user = new User();
        user.setName(dto.getName());
        user.setIdCard(dto.getIdCard());
        user.setPhone(dto.getPhone());
        user.setBirthDate(dto.getBirthDate());
        user.setGender(dto.getGender());

        // 设置岗位和片区
        if (dto.getPositionId() != null) {
            Option position = optionRepository.findById(dto.getPositionId()).orElse(null);
            user.setPosition(position);
        }
        if (dto.getAreaId() != null) {
            Option area = optionRepository.findById(dto.getAreaId()).orElse(null);
            user.setArea(area);
        }

        user.setStatus(User.UserStatus.PENDING);

        userRepository.save(user);
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new BusinessException(ResultCode.USERNAME_PASSWORD_ERROR));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_PASSWORD_ERROR);
        }

        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_ACTIVATED);
        }

        // 生成JWT token
        String token = jwtUtil.generateToken(user.getUsername());

        UserVO userVO = convertToVO(user);
        return new LoginVO(token, userVO);
    }

    @Override
    public List<UserVO> getPendingUsers() {
        return userRepository.findByStatus(User.UserStatus.PENDING)
                .stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserVO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public UserVO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));
        return convertToVO(user);
    }

    @Override
    @Transactional
    public void activateAccount(String userId, String roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ResultCode.ROLE_NOT_FOUND));

        // 设置账号名为手机号,密码为123456
        user.setUsername(user.getPhone());
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole(role);
        user.setStatus(User.UserStatus.ACTIVE);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode("123456"));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void setRole(String userId, String roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ResultCode.ROLE_NOT_FOUND));

        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);

        if (user.getPosition() != null) {
            vo.setPositionId(user.getPosition().getId());
            vo.setPositionName(user.getPosition().getTitle());
        }
        if (user.getArea() != null) {
            vo.setAreaId(user.getArea().getId());
            vo.setAreaName(user.getArea().getTitle());
        }
        if (user.getRole() != null) {
            vo.setRoleId(user.getRole().getId());
            vo.setRoleName(user.getRole().getRoleName());
        }
        vo.setStatus(user.getStatus().name());

        return vo;
    }
}

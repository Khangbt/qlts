package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.common.Constants;
import com.hust.qlts.project.common.ExcelDataUtil;
import com.hust.qlts.project.dto.*;
import com.hust.qlts.project.entity.HumanResourcesEntity;
import com.hust.qlts.project.entity.excel.HumanResourceExcel;
import com.hust.qlts.project.repository.customreporsitory.HistoryCustomRepository;
import com.hust.qlts.project.repository.customreporsitory.HumanResourcesCustomRepository;
import com.hust.qlts.project.repository.jparepository.HumanResourcesRepository;
import com.hust.qlts.project.repository.jparepository.PartRepository;
import com.hust.qlts.project.repository.jparepository.PositionRepository;
import com.hust.qlts.project.service.HumanResourcesService;
import com.hust.qlts.project.service.mapper.HumanResourcesMapper;
import common.ErrorCode;
import common.ResultResp;
import exception.CustomExceptionHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service(value = "humanResourcesService")
public class HumanResourcesServiceImpl implements HumanResourcesService, UserDetailsService {

    private final Logger log = LogManager.getLogger(HumanResourcesServiceImpl.class);

    @Autowired
    private HumanResourcesCustomRepository customRepository;

    @Autowired
    private HumanResourcesRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HumanResourcesMapper humanResourcesMapper;



    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PositionRepository positionRepository;




    @Autowired
    private HistoryCustomRepository historyCustomRepository;


    @Autowired
    private BCryptPasswordEncoder passwordEncode;

    @Autowired
    private JavaMailSender javaMailSender;

    @Qualifier("freeMarkerConfiguration")
    @Autowired
    private Configuration config;



    @Autowired
    private HumanResourcesRepository humanResourcesRepository;



    private static final boolean REQUIRED = true;
    private static final boolean NOT_REQUIRED = false;
    private List<IPartDTO> listPart = new ArrayList<>();
    private List<IPositionDTO> listPosition = new ArrayList<>();

    //AnhTT_IIST
    @Override
    public List<HumanResourcesDTO> getListHumanResourceByNameOrCode(DTOSearch dto) {
        Long positionID = null;

        List<HumanResourcesEntity> listEntity ;
        if (null == dto.getType() || "".equals(dto.getType())) {
            listEntity = humanResourcesRepository.findAllByUsernameOrGmail(dto.getKeySearch());
        } else {
            listEntity = humanResourcesRepository.findAllByUsernameOrGmailAndPosition(dto.getKeySearch(), positionID);
        }

        return humanResourcesMapper.toDto(listEntity);
    }

    @Override
    public HumanResourcesDTO getUserInfo(String username) {
        log.info("--------------------get user info by username-----------------");
        HumanResourcesEntity humanResourcesEntity = repository.findByEmail2(username);
        HumanResourcesDTO humanResourcesDTO = new HumanResourcesDTO();
        BeanUtils.copyProperties(humanResourcesEntity, humanResourcesDTO);

        log.info("--------------------set list permission to user-----------------");
        List<String> lstPermission = customRepository.getListPermissionByUserId(humanResourcesEntity.getHumanResourceId());
        if (CollectionUtils.isNotEmpty(lstPermission)) {
            humanResourcesDTO.setLstPermission(lstPermission);
        }
        return humanResourcesDTO;
    }

    @Override
    public List<HumanResourcesDTO> getListHumanResourceByProjectID(long i) {
        return null;
    }

    //hungnv
    @Override
    public Long changePassword(HumanResourcesDTO humanResourcesDTO) {

        String password = humanResourcesDTO.getPassword();
        String newPassword = humanResourcesDTO.getNewPassword();
        String newPasswordConfirm = humanResourcesDTO.getNewPasswordConfirm();
        if (!newPassword.equals(newPasswordConfirm)) {
            throw new CustomExceptionHandler("khong_trung", HttpStatus.BAD_REQUEST);
        }
        try {
            HumanResourcesEntity entity = repository.findByUsername(humanResourcesDTO.getUsername());
            if (!BCrypt.checkpw(password, entity.getPassword())) {
                throw new CustomExceptionHandler("sai_password", HttpStatus.BAD_REQUEST);
            }
            entity.setPassword(passwordEncoder.encode(newPassword));
            entity.setIsNew(common.Constants.IS_NOT_NEW);
            log.info("update password");
            HumanResourcesEntity result = repository.save(entity);
            log.info("doi pass thanh cong");
            return result.getHumanResourceId();
        } catch (Exception ex) {
            log.error("doi khong duoc", ex);
            throw ex;
        }


    }

    @Override
    public Long checkPassword(HumanResourcesDTO humanResourcesDTO) {
        HumanResourcesEntity entity = repository.findByUsername(humanResourcesDTO.getUsername());
        if (!BCrypt.checkpw(humanResourcesDTO.getPassword(), entity.getPassword())) {
            throw new CustomExceptionHandler("sai_password", HttpStatus.BAD_REQUEST);
        }
        return entity.getHumanResourceId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        HumanResourcesEntity humanResourcesEntity = repository.findByEmail2(email);
        if (humanResourcesEntity == null) {
            throw new CustomExceptionHandler(ErrorCode.USERNAME_NOT_FOUND.getCode(), HttpStatus.UNAUTHORIZED);
        }
        List<GrantedAuthority> roleList = new ArrayList<>();
        roleList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        humanResourcesEntity.setAuthorities(roleList);
        return new org.springframework.security.core.userdetails.User(humanResourcesEntity.getEmail(), humanResourcesEntity.getPassword(), humanResourcesEntity.getAuthorities());
    }
    /*duc impl*/
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = CustomExceptionHandler.class)
    public HumanResourcesDTO create(String username, HumanResourcesDTO humanResourcesDTO) {
        HumanResourcesEntity humanResourcesEntity1=new HumanResourcesEntity();
        HumanResourcesEntity humanResourcesEntity = repository.findByCode(humanResourcesDTO.getCode());
        if (null != humanResourcesEntity && humanResourcesDTO.getHumanResourceId() == null) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_FALSE.getCode(), HttpStatus.BAD_REQUEST);
        } else if (null != humanResourcesEntity) {
            HumanResourcesEntity oldEntity = new HumanResourcesEntity();
            oldEntity.setCode(humanResourcesEntity.getCode());
            oldEntity.setEmail(humanResourcesEntity.getEmail());
            oldEntity.setFullName(humanResourcesEntity.getFullName());
            oldEntity.setPositionId(humanResourcesEntity.getPositionId());
            oldEntity.setPartId(humanResourcesEntity.getPartId());
            oldEntity.setStatus(humanResourcesEntity.getStatus());
//            oldEntity.setDateRecruitment(humanResourcesEntity.getDateRecruitment());
            oldEntity.setDateGraduate(humanResourcesEntity.getDateGraduate());
//            oldEntity.setDateMajor(humanResourcesEntity.getDateMajor());
            oldEntity.setNote(humanResourcesEntity.getNote());

            humanResourcesEntity.setCode(humanResourcesDTO.getCode());
            humanResourcesEntity.setEmail(humanResourcesDTO.getEmail());
            humanResourcesEntity.setFullName(humanResourcesDTO.getFullName());
            humanResourcesEntity.setPositionId(humanResourcesDTO.getPositionId());
            humanResourcesEntity.setPartId(humanResourcesDTO.getPartId());
            humanResourcesEntity.setStatus(humanResourcesDTO.getStatus());
//            humanResourcesEntity.setDateRecruitment(humanResourcesDTO.getDateRecruitment());
            humanResourcesEntity.setDateGraduate(humanResourcesDTO.getDateGraduate());
//            humanResourcesEntity.setDateMajor(humanResourcesDTO.getDateMajor());
            humanResourcesEntity.setNote(humanResourcesDTO.getNote());

            humanResourcesEntity.setIsNew(1);

            /*check email changed*/
            if (!oldEntity.getEmail().equals(humanResourcesEntity.getEmail())) {
                sendMailChangeEmail(humanResourcesDTO, oldEntity);
            }
            humanResourcesEntity1 = repository.save(humanResourcesEntity);


        } else if (humanResourcesDTO.getHumanResourceId() == null) {

            humanResourcesEntity = humanResourcesMapper.toEntity(humanResourcesDTO);
            String usernameFE = humanResourcesDTO.getUsername();
            if (humanResourcesRepository.findByUsername(usernameFE) != null) {
                usernameFE = autoGenerateEmail(humanResourcesDTO.getUsername());
            }
            humanResourcesEntity.setIsNew(1);
            humanResourcesEntity.setUsername(usernameFE);

            humanResourcesEntity1 = repository.save(humanResourcesEntity);

        }


        return humanResourcesMapper.toDto(humanResourcesEntity1);
    }

    //send mail change email
    @Override
    public void sendMailChangeEmail(HumanResourcesDTO humanResourcesDTO, HumanResourcesEntity oldEmail) {
        MimeMessage message = javaMailSender.createMimeMessage();
        Map<String, Object> models = new HashMap<>();
        //put new email
        models.put("newEmail", humanResourcesDTO.getEmail());
        //put link
        models.put("link", Constants.URL_WEBAPP);
        //send mail
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Template template = config.getTemplate("change-email-human-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, models);

            helper.setTo(humanResourcesDTO.getEmail());
            helper.setSubject("Thông báo thay đổi email đăng nhập hệ thống Quản lý dự án");
            helper.setText(html, true);
            javaMailSender.send(message);
            log.info("<------------------Send mail auto success-------------->");
        } catch (MessagingException | IOException | TemplateException exception) {
            exception.getMessage();
            log.info("<-----------------------send mail error-------", exception.getMessage());
        }
    }


    @Override
    public HumanResourcesDTO update(HumanResourcesDTO humanResourcesDTO) {
        HumanResourcesEntity hrResourcesEntity = humanResourcesMapper.toEntity(humanResourcesDTO);
        hrResourcesEntity = repository.save(hrResourcesEntity);
        return humanResourcesMapper.toDto(hrResourcesEntity);
    }


//
//    @Override
//
//    public List<IDepartmentDTO> getDepartment() {
//        return null;
//    }

    @Override

    public List<IPartDTO> getPart() {
        return partRepository.getPart();
    }



    @Override

    public List<IPositionDTO> getPosition() {
        return positionRepository.getPosition();
    }

    @Override
    public HumanResourcesDTO findById(Long Id) {
        if (!repository.findById(Id).isPresent()) {
            throw new CustomExceptionHandler(ErrorCode.USERNAME_NOT_FOUND.getCode(), HttpStatus.BAD_REQUEST);
        }
        return humanResourcesMapper.toDto(repository.findById(Id).get());
    }

    @Override
    public List<HistoryDTO> getHumanHistory() {
        return historyCustomRepository.getHumanHistory();
    }

    @Override
    public List<HistoryDTO> getHumanHistoryById(Long Id) {
        return historyCustomRepository.getHumanHistoryById(Id);
    }

    @Override
    public void humanHistory(String username, HumanResourcesEntity oldEntity, HumanResourcesEntity newEntity) {

    }

    //tan
    public void humanHistoryCustom(String username, int status, HumanResourcesEntity oldEntity, HumanResourcesEntity newEntity) {

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = CustomExceptionHandler.class)
    public HumanResourcesDTO findByCode(String code) {
        if (null != repository.findByCode(code)) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_EXIST.getCode(), HttpStatus.BAD_REQUEST);
        }
        return humanResourcesMapper.toDto(repository.findByCode(code));
    }
    @Override
    public List<HumanResourcesDTO> findByEmail(String email) {

        if (!repository.findByEmail(email).isEmpty()) {
            throw new CustomExceptionHandler(ErrorCode.EMAIL_IS_EXIST.getCode(), HttpStatus.BAD_REQUEST);
        }

        return humanResourcesMapper.toDto(repository.findByEmail(email));
    }

    @Override
    public HumanResourcesDTO getByEmail(String email) {
        HumanResourcesEntity entity = repository.findByEmail2(email);
        if (null == entity) {
            throw new CustomExceptionHandler(common.Constants.EMAIL_NOT_FOUND, HttpStatus.BAD_REQUEST, ErrorCode.EMAIL_NOT_FOUND);
        }
        return humanResourcesMapper.toDto(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultResp resetPassword(Long humanResourceID, String usernameAdmin) {
        log.info("---> RESET PASSWORD: UserID " + humanResourceID + " confirm reset password START");
        HumanResourcesEntity humanResource = repository.findById(humanResourceID).get();
        String olPassWord = humanResource.getPassword();
        // generate random password
        String SALTCHARS = "ABCDEefgh!@ijklFGH123IJKL!@#$MNOPQRS012345TUVWXYZabcdmnopqrstuvwxyz6789%^&*";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 15) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        try {
            humanResource.setPassword(passwordEncode.encode(saltStr));
        } catch (Exception ex) {
            log.error("<--- Reset Password Fail by Error: ", ex.getMessage());
            ex.printStackTrace();
        }
//        Long idAdmin = repository.findByUsername(usernameAdmin).getHumanResourceId();
//        humanResource.setUpdateBy(idAdmin);
        humanResource.setUpdateDate(new Date());
        repository.save(humanResource);
        if (StringUtils.isNotBlank(olPassWord)) {
            humanHistoryCustom(usernameAdmin, 8, humanResource, null);
        } else
            humanHistoryCustom(usernameAdmin, 9, humanResource, null);

        log.info("<--- Reset Password Complete");
        log.info("<--- Send email notification to user start!");
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(humanResource.getEmail()));
            message.setSubject("THÔNG TIN TÀI KHOẢN HỆ THỐNG QUẢN LÝ DỰ ÁN", "UTF-8");
            String subject = "Kính gửi anh/chị,\n\n" +
                    "Hệ thống Quản lý dự án của CÔNG TY TNHH GIẢI PHÁP VÀ CÔNG NGHỆ TÍCH HỢP ĐÔNG DƯƠNG gửi đến anh chị thông tin như sau:\n" +
                    "Anh/chị đã được reset mậu khẩu:\n" +
                    "Link truy cập hệ thống:" + Constants.URL_WEBAPP + "\n" +
                    "Họ và tên: " + humanResource.getFullName() + "\n" +
                    "Tên đăng nhập: " + humanResource.getEmail() + "\n" +
                    "Mật khẩu mới: " + saltStr + "\n\n" +
                    "Trân trọng!";
            message.setText(subject, "UTF-8");
            javaMailSender.send(message);
            log.info("<--- Send email success!");
            return ResultResp.success(humanResource);

        } catch (MessagingException | MailException ex) {
            log.error("Send email notification fail by Error ", ex.getMessage());
            return ResultResp.badRequest(ErrorCode.RESET_PASSWORD_FAIL);
        }
    }

    public String autoGenerateEmail(String username) {
        int i = 1;
        while (true) {
            HumanResourcesEntity entity = humanResourcesRepository.findByUsername(username + i);
            if (entity == null) {
                return username + i;
            }
            i++;
        }
    }

    /*end duc*/

    // TanNV
    @Override
    public DataPage<HumanResourcesShowDTO> getPageHumanResourcesSeach(HumanResourcesShowDTO dto) {
        log.info("-----------------Danh sach nhan su--------------");

        DataPage<HumanResourcesShowDTO> data = new DataPage<>();
        dto.setPage((null == dto.getPage()) ? 1 : dto.getPage());
        dto.setPageSize((null == dto.getPageSize()) ? 10 : dto.getPageSize());


//        LocalDate today = LocalDate.now();
//        int currentYear = today.getYear();
//        int a = 0;
////        if (dto.getExperience() != null) {
////            a = currentYear - dto.getExperience();
////            dto.setExperience(a);
////        }
        List<HumanResourcesShowDTO> listProject = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(customRepository.getlistHumanResources(dto))) {
            listProject = customRepository.getlistHumanResources(dto);
            data.setData(listProject);
        }
        data.setPageIndex(dto.getPage());
        data.setPageSize(dto.getPageSize());
        data.setDataCount(dto.getTotalRecord());
        data.setPageCount(dto.getTotalRecord() / dto.getPageSize());
        if (data.getDataCount() % data.getPageSize() != 0) {
            data.setPageCount(data.getPageCount() + 1);
        }
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteHumanResources(Long id, String name) {
        log.info("-----------------Xoa nhan su---------------");
        if (id != null) {
            HumanResourcesEntity humanResourcesEntity = repository.findByHumanResourceId(id);
            if (customRepository.checkAssociationBeforeDelete(id)) {
                humanResourcesEntity.setStatus(3);
                repository.save(humanResourcesEntity);
                humanHistoryCustom(name, 5, humanResourcesEntity, null);
                log.info("<--- DELETE HUMAN_RESOURCES COMPLETE");
                return true;
            } else {
                log.error("<--- CAN'T DELETE HUMAN RESOURCES");
                return false;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean lockHumanResources(Long id, String name) {
        log.info("-----------------khoa nhan su---------------");
        if (id != null) {
            HumanResourcesEntity humanResourcesEntity = repository.findByHumanResourceId(id);
            if (humanResourcesEntity.getStatus() == 2) {
                humanResourcesEntity.setStatus(1);
                repository.save(humanResourcesEntity);
                humanHistoryCustom(name, 7, humanResourcesEntity, null);
                log.info("<--- Unlock Human Resources with id = " + id);
                return true;
            } else if (humanResourcesEntity.getStatus() == 1) {
//                if (customRepository.checkAssociationBeforeDelete(id)) {
                humanResourcesEntity.setStatus(2);
                repository.save(humanResourcesEntity);
                humanHistoryCustom(name, 6, humanResourcesEntity, null);
                log.info("<--- LOCK HUMAN_RESOURCES COMPLETE");
                return true;
//                } else {
//                    log.error("<--- CAN'T DELETE HUMAN RESOURCES");
//                    return false;
//                }
            }
        }
        return false;
    }

    @Override
    public Integer getActiveFromHumanResourceId(Long id) {
        return repository.findById(id).get().getStatus();
    }

    @Override
    public List<HumanResourcesDTO> getHumanResources(DTOSearch dto) {

        log.info("-----------------lay danh sach app-param theo partype va key search----------------");
        List<HumanResourcesDTO> HumanResourcesList = customRepository.getHumanResources(dto);

        if (CollectionUtils.isNotEmpty(HumanResourcesList)) {
            return HumanResourcesList;
        } else {
            return new ArrayList<>();
        }

    }

    /*
     *@author ThaoLC - IIST
     *created on 9/9/2020
     */
    @Override
    public List<HumanResourcesDTO> getListHumanResources(Long projectId) {
      return  null;
    }

    @Override
    public String getLeaderNameFromProject(Long projectId) {
        return null;
    }


    @Override
    public byte[] getFileImport(List<HumanResourcesImportDTO> list) {
        log.info("---------------------------improt file------------");
        for (HumanResourcesImportDTO dto : list) {
            int xet = 0;
            StringBuilder buffer = new StringBuilder();

            if (dto.getCode() == null) {
                buffer.append("Trường code không được để trống ");
                xet++;
            } else if (dto.getEmail() == null) {
                buffer.append("Trường email đang để trống ");
                xet++;
            } else if (dto.getDepartment() == null) {
                buffer.append("Trường phòng ban đang để trống ");
                xet++;
            } else if (dto.getDateRecuitment() == null) {
                buffer.append("Trường ngày tuyển dụng đang để trống");
                xet++;
            } else if (dto.getPosition() == null) {
                buffer.append("Trường chức danh đang để trống");
                xet++;
            } else if (dto.getFullName() == null) {
                buffer.append("Trường tên nhân sự đang để trống");
                xet++;
            } else if (dto.getPart() == null) {
                buffer.append("Trường bộ phận đang để trống");
                xet++;
            } else {
                HumanResourcesEntity entity = entity(dto);
                if (null != repository.findByCode(dto.getCode())) {
                    buffer.append("Mã nhân sự đã tồn tại");
                    xet++;
                }
                // xet chuc danh(chuc vu)
                if (dto.getPosition() != null && positionRepository.getName(dto.getPosition().toUpperCase()).size() == 1) {
                    entity.setPositionId(positionRepository.getName(dto.getPosition().toUpperCase()).get(0).getId());
                }
                // xet Bộ phận
                if (dto.getPart() != null && partRepository.getName(dto.getPart().toUpperCase()).size() == 1) {
                    entity.setPartId(partRepository.getName(dto.getPart().toUpperCase()).get(0).getId());
                }
                // xet phong ban
//                if (dto.getDepartment() != null && departmentRepository.getName(dto.getDepartment().toUpperCase()).size() == 1) {
//                    entity.setDepartmentId(departmentRepository.getName(dto.getDepartment().toUpperCase()).get(0).getId());
//                }
                // xet thời gian tuyển dụng
//                Date date = entity.getDateRecruitment();
//                Date currentDate = new Date();
//                if (!date.before(currentDate)) {
//                    buffer.append("Ngày tuyển dụng nhỏ hơn ngày hiện tại");
//                    xet++;
//                }

                if (dto.getDepartment().equals("DEV")) {
                    if (dto.getMajor() == null) {
                        buffer.append("Khi phòng ban là DEV thì phòng chuyên môn không được bỏ trống");
                        xet++;
                    }
                }
                // xet chuc danh(chuc vu) lỗi
                else if (dto.getPosition() != null && positionRepository.getName(dto.getPosition().toUpperCase()).size() == 0) {
                    buffer.append("Không có chức vụ này trong công ty ");
                    xet++;
                }
                // xet Bộ phận lỗi

                else if (dto.getPart() != null && partRepository.getName(dto.getPart().toUpperCase()).size() == 0) {
                    buffer.append("Không có bộ phận này trong công ty  ");
                    xet++;
                }
                // xet phong ban lỗi
//                else if (dto.getDepartment() != null && departmentRepository.getName(dto.getDepartment().toUpperCase()).size() == 0) {
//                    buffer.append("Không có phòng ban này trong công ty ");
//                    xet++;
//                }

            }
            if (xet == 0) {
                dto.setStaus("Đã thêm bảng ghi thành công");
            } else {
                dto.setStaus(buffer.toString());
            }

        }
        return writeFile(list);
    }


    private HumanResourcesEntity entity(HumanResourcesImportDTO dto) {
        HumanResourcesEntity entity = new HumanResourcesEntity();
        entity.setCode(dto.getCode());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setStatus(1);
        entity.setIsNew(1);


//        String sDate = dto.getDateRecuitment();
//        if (sDate.indexOf('/') >= 0) {
//            try {
//                Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
//                entity.setDateRecruitment(date1);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Date javaDate = DateUtil.getJavaDate((Double.parseDouble(dto.getDateRecuitment())));
//
//            entity.setDateRecruitment(javaDate);
//        }
//        if (null == dto.getDateMajor()) {
//            entity.setDateMajor(null);
//        } else {
//            int dateMajor = Integer.parseInt(dto.getDateMajor());
//            entity.setDateMajor(dateMajor);
//        }
//        entity.setProjectPaticipate(dto.getProjectParticipate());

        if (null == dto.getDateGraduate()) {
            entity.setDateGraduate(null);
        } else {
            entity.setDateGraduate(Integer.parseInt(dto.getDateGraduate()));
        }

        entity.setNote(dto.getNode());

        return entity;
    }

    private String getFileFromURL(String path) {
        URL url = this.getClass().getResource(path);
        assert url != null;
        return url.getPath();
    }

    private byte[] writeFile(List<HumanResourcesImportDTO> list) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        InputStream in;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in = new FileInputStream(new File(getFileFromURL("/templates/import/saveout.xlsx")));
        } catch (FileNotFoundException e) {
            return null;
        }
        for (HumanResourcesImportDTO dto : list) {
            if (dto.getDateRecuitment() != null) {
                String data = formatter.format(DateUtil.getJavaDate((Double.parseDouble(dto.getDateRecuitment()))));
                dto.setDateRecuitment(data);
            }
        }
        HashedMap beans = new HashedMap();
        beans.put("customs", list);
        try {
            XLSTransformer transformer = new XLSTransformer();
            org.apache.poi.ss.usermodel.Workbook workBook = transformer.transformXLS(in, beans);
            workBook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public byte[] importExcel(MultipartFile file) throws IOException {
        listPosition = getPosition();


        listPart = getPart();
        Workbook workbook = null;
        try {
            //TODO: save lai file user import -> doc lai ghi file ket qua
            File fileUserImport = ExcelDataUtil.saveFile(file);

            workbook = WorkbookFactory.create(fileUserImport);

            //TODO: lay data tu file user import
            @SuppressWarnings("unchecked")
            List<HumanResourceExcel> dataExcel = (List<HumanResourceExcel>)
                    ExcelDataUtil.getListInExcelFile(fileUserImport, new HumanResourceExcel(), workbook);

            //TODO: validate va import -> Map<long, ket qua import>
            Map<Long, String> listResult = new HashMap<>();

            listResult = doValidate(dataExcel);

            //TODO: ghi lai file ket qua
            String fileNameKQ = "KQ_IMPORT_NHAN_SU";

            ExcelDataUtil.writeResultExcels(workbook, listResult, fileNameKQ, dataExcel);

            File fileResult = FileUtils.getFile("./report_out/" + fileNameKQ);

            return Files.readAllBytes(fileResult.toPath());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            if (!Objects.isNull(workbook)) workbook.close();
        }
    }

    @Override
    public List<HumanResourcesDTO> findAllHumanResourcesByPositionId(Long id) {
        try {
            List<HumanResourcesEntity> entities = humanResourcesRepository.findAllHumanResourcesByPositionId(id);
            return humanResourcesMapper.toDto(entities);
        } catch (Exception e) {
            log.error(ErrorCode.OBJ_NOT_FOUND.getMsgError());
            log.error(e);
            throw new CustomExceptionHandler(ErrorCode.OBJ_NOT_FOUND.getMsgError(), HttpStatus.BAD_REQUEST, ErrorCode.OBJ_NOT_FOUND);
        }
    }

    private Map<Long, String> doValidate(List<HumanResourceExcel> dataExcel) throws ParseException {
        Map<Long, String> mappingError = new HashMap<>();
        List<HumanResourcesEntity> humanResourcesEntityList = new ArrayList<>();
        long index = 0;
        for (HumanResourceExcel excel : dataExcel) {
            index++;
            if (isVlid(excel, mappingError, index - 1)) {
                // convert excel to entity -> add list entity
                HumanResourcesEntity entity = convert(excel);
                humanResourcesEntityList.add(entity);
                repository.save(entity);
                mappingError.put(index - 1, "Import thành công");

            }
        }
        // save all entity
//        repository.saveAll(humanResourcesEntityList);
        return mappingError;
    }

    private HumanResourcesEntity convert(HumanResourceExcel excel) throws ParseException {

        HumanResourcesEntity entity = new HumanResourcesEntity();
        entity.setCode(excel.getEmployeeCode().trim());
        entity.setFullName(excel.getEmployeeName().trim());
        entity.setStatus(1);
        entity.setIsNew(1);
        for (IPositionDTO item : listPosition) {
            if (item.getName().equalsIgnoreCase(excel.getPosition())) {
                entity.setPositionId(item.getId());
            }
        }
        for (IPartDTO iPartDTO : listPart) {
            if (iPartDTO.getName().equalsIgnoreCase(excel.getPart())) {
                entity.setPartId(iPartDTO.getId());
            }
        }
//        for (IDepartmentDTO iDepartmentDTO : listDeparmentDTO) {
//            if (iDepartmentDTO.getName().equalsIgnoreCase(excel.getDeparment())) {
//                entity.setDepartmentId(iDepartmentDTO.getId());
//            }
//        }
//        for (IMajorDTO iMajorDTO : listMajor) {
//            if (iMajorDTO.getName().equalsIgnoreCase(excel.getMajor())) {
//                entity.setMajorId(iMajorDTO.getId());
//            }
//        }
//        Date DateRecruitment = new SimpleDateFormat("dd/MM/yyyy").parse(excel.getDaterecruitment());
//        entity.setDateRecruitment(DateRecruitment);


//        if (!Objects.isNull(excel.getDatemajor()) && !Strings.isEmpty(excel.getDatemajor()))
//            entity.setDateMajor(Integer.parseInt(excel.getDatemajor()));
//        else
//            entity.setDateMajor(null);

        if (!Objects.isNull(excel.getDategraduate()) && !Strings.isEmpty(excel.getDategraduate()))
            entity.setDateGraduate(Integer.parseInt(excel.getDategraduate()));
        else entity.setDateGraduate(null);

//        entity.setProjectPaticipate(excel.getProjectparticipate().trim());
        entity.setEmail(excel.getEmail().trim());
        entity.setNote(excel.getNote().trim());
        String userName = "";
        String s = excel.getEmail();
        for (int i = 0; i < excel.getEmail().length(); i++) {
            if (excel.getEmail().charAt(i) == '.' || excel.getEmail().charAt(i) == '@') {
                break;
            } else {
                userName = userName + excel.getEmail().charAt(i);
            }
        }
        if (humanResourcesRepository.findByUsername(userName) != null) {
            userName = autoGenerateEmail(userName);
        }
        entity.setUsername(userName);


        return entity;
    }

    private boolean isVlid(HumanResourceExcel excel, Map<Long, String> mappingError, long index) throws ParseException {
        StringBuilder errorMessage = new StringBuilder();
        // validate truong ma so nhan vien
        if (validateCodeString(excel.getEmployeeCode(), REQUIRED, excel.getEmployeeCode().length(), "Mã số nhân sự ", errorMessage)) {
        } else if (
            //valide truong ten nhan su
                validateNameString(excel.getEmployeeName(), REQUIRED, 50, "Họ và tên ", errorMessage)) {
        } else if (

            //valide chuc danh
                validatePosition(excel.getPosition(), REQUIRED, 50, "Chức danh ", errorMessage)) {
        } else if (

            //valide bộ phận
                validatepart(excel.getPart(), REQUIRED, 50, "Bộ phận ", errorMessage)) {
        } else if (

            //valide phòng ban
                validatedepartmentRepository(excel.getDeparment(), REQUIRED, 50, "Phòng ban ", errorMessage)) {
        } else if (

            //valide chuyen mon
                validatemajor(excel.getMajor(), excel.getDeparment(), REQUIRED, 50, "Chuyên môn ", errorMessage)) {
        } else if (

            //valide thoi gian tuyen dung
                validateDateRecruitment(excel.getDaterecruitment(), REQUIRED, 50, "Ngày tuyển dụng ", errorMessage)) {
        } else if (
            //valide email
                validateEmail(excel.getEmail(), REQUIRED, excel.getEmail().length(), "Email ", errorMessage)) {
        }
//        else{
//            errorMessage.append("Import thành công");
//
//        }


        mappingError.put(index, errorMessage.toString());

        // truong hop co loi thi tra ve false
        return Strings.isEmpty(errorMessage.toString());
    }

    // validate code
    private boolean validateCodeString(String value, boolean requied, long maxlength, String fieldName, StringBuilder errorMessage) {
        if (Objects.isNull(value) || Strings.isEmpty(value)) {
            errorMessage.append(fieldName).append("không được để trống");
            return true;
        } else {
            // tra ma loi la bat buoc
            Pattern regex = Pattern.compile("^[a-zA-Z0-9]+$");
//            String s = "th321355551thlllxxxo";
            Matcher matcher = regex.matcher(value);
            if (!Objects.isNull(repository.findByCode(value))) {
                errorMessage.append(fieldName).append("đã tồn tại");
                return true;
            }
            else if (!matcher.find()){
                errorMessage.append(fieldName).append("không chứa dấu cách, tiếng việt có dấu, ký tự đặc biệt");
                return true;
            }else  if (maxlength >=50){
                errorMessage.append(fieldName).append("không được phép nhập lớn hơn 50 ký tự");
                return true;
            }
            else
                return false;

        }
    }


    // validate name humanresouce
    private boolean validateNameString(String value, boolean requied, long maxlength, String fieldName, StringBuilder errorMessage) {
        if (Objects.isNull(value) || Strings.isEmpty(value)) {
            errorMessage.append(fieldName).append("không được để trống");
            return true;
        } else {
            // tra ma loi la bat buoc
            return false;
        }
    }

    // validate  xet chuc danh(chuc vu) position
    private boolean validatePosition(String value, boolean requied, long maxlength, String fieldName, StringBuilder errorMessage) {
        if (Objects.isNull(value) || Strings.isEmpty(value)) {
            // tra ma loi la bat buoc
            errorMessage.append(fieldName).append("không được để trống");
            return true;
        } else {
            listPosition = positionRepository.getName(value.toUpperCase());
            if (positionRepository.getName(value.toUpperCase()).size() == 0) {
                errorMessage.append(fieldName).append("không tồn tại");
                return true;
            }
        }
        return false;
    }

    // validate  bo phan part
    private boolean validatepart(String value, boolean requied, long maxlength, String fieldName, StringBuilder errorMessage) {
        if (Objects.isNull(value) || Strings.isEmpty(value)) {
            // tra ma loi la bat buoc
            errorMessage.append(fieldName).append("không được để trống");
            return true;
        } else {
            listPart = partRepository.getName(value.toUpperCase());
            if (partRepository.getName(value.toUpperCase()).size() == 0) {
                errorMessage.append(fieldName).append("không tồn tại");
                return true;
            }

        }
        return false;
    }

    // validate  phong ban departmentRepository
    private boolean validatedepartmentRepository(String value, boolean requied, long maxlength, String fieldName, StringBuilder errorMessage) {
//        if (Objects.isNull(value) || Strings.isEmpty(value)) {
//            // tra ma loi la bat buoc
//            errorMessage.append(fieldName).append("không được để trống");
//            return true;
//        } else {
//            listDeparmentDTO = departmentRepository.getName(value.toUpperCase());
//            if (departmentRepository.getName(value.toUpperCase()).size() == 0) {
//                errorMessage.append(fieldName).append("không tồn tại");
//                return true;
//            }
//        }
        return true;
    }

    // validate  thời gian tuyển dụng
    private boolean validateDateRecruitment(String value, boolean requied, long maxlength, String fieldName, StringBuilder errorMessage) throws ParseException {
        if (Objects.isNull(value) || Strings.isEmpty(value)) {
            // tra ma loi la bat buoc
            errorMessage.append(fieldName).append("không được để trống");
            return true;
        } else {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(value);
            Date currentDate = new Date();
            if (!date.before(currentDate)) {
                errorMessage.append(fieldName).append("phải nhỏ hơn hặc bằng ngày hiện tại");
                return true;
            }
        }
        return false;
    }

    // validate chuyen mon major
    private boolean validatemajor(String value, String deparment, boolean requied, long maxlength, String fieldName, StringBuilder errorMessage) {


        return true;
}

    // validate  Email
    private boolean validateEmail(String value, boolean requied, long maxlength, String fieldName, StringBuilder errorMessage) {
        boolean a = Objects.isNull(value);
        boolean b = Strings.isEmpty(value);
        if (Objects.isNull(value) || Strings.isEmpty(value)) {
            errorMessage.append(fieldName).append("không được để trống");
            return true;
        } else {
            // tra ma loi la bat buoc
            String email = value;
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == '.' || value.charAt(i) == '@') {
                    email = email.substring(i);
                    break;
                }
            }
            boolean bs = Objects.isNull(repository.findByEmail1(value));
            if (repository.findByEmail1(value).size() != 0) {
                errorMessage.append(fieldName).append("đã tồn tại");
                return true;
            }
            else if (email.equals(".iist@gmail.com") || email.equals("@iist.vn") || email.equals("@iist.com.vn")) {
                return false;
            } else if (maxlength >= 50){
                errorMessage.append(fieldName).append("không được phép nhập lớn hơn 50 ký tự");
                return true;
            }else {
                errorMessage.append(fieldName).append("không hợp lệ");
                return true;
            }
        }
    }


    @Override
    public List<ICusTomDto> listAll() {
        return repository.getcoe();
    }
}

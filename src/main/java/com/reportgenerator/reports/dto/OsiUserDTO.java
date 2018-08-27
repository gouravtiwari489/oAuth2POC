package com.reportgenerator.reports.dto;

// Generated Dec 1, 2016 5:20:37 PM by Hibernate Tools 3.4.0.CR1

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OsiUserDTO implements java.io.Serializable {

  private Integer id;
  private String userName;
  private String password;
  private String firstName;
  private String lastName;
  private String emailId;
  private String empNumber;
  private String mobileNumber;
  private Date startDate;
  private String fullName;
  private Date endDate;
  private String photoPath;
  private Integer createdBy;
  private Date createdDate;
  private Integer updatedBy;
  private Date updatedDate;
  private String menuTree;
  private String token;
  private Integer active;
  private Integer hasDefaultPwd;
  private List<Integer> ids;
  private Integer businessGroupId;
  private List<String> adminEmailIds;
  private String hostOrIp;
  private boolean isManager;
  private boolean isSkillAdded;
  private Integer orgId;
  private String orgCode;
  /*private Set<OsiUserLoginsDTO> osiUserLoginses = new HashSet<OsiUserLoginsDTO>(0);
  private Set<OsiUserFuncExclDTO> osiUserFuncExcls = new HashSet<OsiUserFuncExclDTO>(0);
  private Set<OsiAttachmentsDTO> osiAttachmentses = new HashSet<OsiAttachmentsDTO>(0);
  private Set<OsiRespUserDTO> osiRespUsers = new HashSet<OsiRespUserDTO>(0);
  private Set<OsiUserOperationExclDTO> osiUserOperationExcls = new HashSet<OsiUserOperationExclDTO>(0);*/
  public OsiUserDTO() {}

  public OsiUserDTO(
      Integer id,
      String userName,
      String password,
      String emailId,
      String mobileNumber,
      Date startDate,
      Date endDate) {
    this.id = id;
    this.userName = userName;
    this.password = password;
    this.emailId = emailId;
    this.mobileNumber = mobileNumber;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public OsiUserDTO(
      Integer id,
      String userName,
      String password,
      String firstName,
      String lastName,
      String emailId,
      String mobileNumber,
      Date startDate,
      String fullName,
      Date endDate,
      Integer createdBy,
      Date createdDate,
      Integer updatedBy,
      Date updatedDate,
      /*Set<OsiUserLoginsDTO> osiUserLoginses,
      Set<OsiUserFuncExclDTO> osiUserFuncExcls,
      Set<OsiAttachmentsDTO> osiAttachmentses,
      Set<OsiRespUserDTO> osiRespUsers,
      Set<OsiUserOperationExclDTO> osiUserOperationExcls,*/
      Integer hasDefaultPwd) {
    this.id = id;
    this.userName = userName;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailId = emailId;
    this.mobileNumber = mobileNumber;
    this.startDate = startDate;
    this.fullName = fullName;
    this.endDate = endDate;
    this.createdBy = createdBy;
    this.createdDate = createdDate;
    this.updatedBy = updatedBy;
    this.updatedDate = updatedDate;
    /*this.osiUserLoginses = osiUserLoginses;
    this.osiUserFuncExcls = osiUserFuncExcls;
    this.osiAttachmentses = osiAttachmentses;
    this.osiRespUsers = osiRespUsers;
    this.osiUserOperationExcls = osiUserOperationExcls;*/
    this.hasDefaultPwd = hasDefaultPwd;
  }

  public List<Integer> getIds() {
    return ids;
  }

  public void setIds(List<Integer> ids) {
    this.ids = ids;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmailId() {
    return this.emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getMobileNumber() {
    return this.mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public Date getStartDate() {
    return this.startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public String getFullName() {
    return this.fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public Date getEndDate() {
    return this.endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Integer getCreatedBy() {
    return this.createdBy;
  }

  public void setCreatedBy(Integer createdBy) {
    this.createdBy = createdBy;
  }

  public Date getCreatedDate() {
    return this.createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Integer getUpdatedBy() {
    return this.updatedBy;
  }

  public void setUpdatedBy(Integer updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Date getUpdatedDate() {
    return this.updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  /*public Set<OsiUserLoginsDTO> getOsiUserLoginses() {
  	return this.osiUserLoginses;
  }

  public void setOsiUserLoginses(Set<OsiUserLoginsDTO> osiUserLoginses) {
  	this.osiUserLoginses = osiUserLoginses;
  }

  public Set<OsiUserFuncExclDTO> getOsiUserFuncExcls() {
  	return this.osiUserFuncExcls;
  }

  public void setOsiUserFuncExcls(Set<OsiUserFuncExclDTO> osiUserFuncExcls) {
  	this.osiUserFuncExcls = osiUserFuncExcls;
  }

  public Set<OsiAttachmentsDTO> getOsiAttachmentses() {
  	return this.osiAttachmentses;
  }

  public void setOsiAttachmentses(Set<OsiAttachmentsDTO> osiAttachmentses) {
  	this.osiAttachmentses = osiAttachmentses;
  }

  public Set<OsiRespUserDTO> getOsiRespUsers() {
  	return this.osiRespUsers;
  }

  public void setOsiRespUsers(Set<OsiRespUserDTO> osiRespUsers) {
  	this.osiRespUsers = osiRespUsers;
  }

  public Set<OsiUserOperationExclDTO> getOsiUserOperationExcls() {
  	return this.osiUserOperationExcls;
  }

  public void setOsiUserOperationExcls(
  		Set<OsiUserOperationExclDTO> osiUserOperationExcls) {
  	this.osiUserOperationExcls = osiUserOperationExcls;
  }*/
  public String getEmpNumber() {
    return empNumber;
  }

  public void setEmpNumber(String empNumber) {
    this.empNumber = empNumber;
  }

  public OsiUserDTO(
      Integer id,
      String userName,
      String password,
      String firstName,
      String lastName,
      String emailId,
      String empNumber,
      String mobileNumber,
      Date startDate,
      String fullName,
      Date endDate,
      Integer createdBy,
      Date createdDate,
      Integer updatedBy,
      Date updatedDate /*, Set<OsiUserLoginsDTO> osiUserLoginses
			Set<OsiUserFuncExclDTO> osiUserFuncExcls, Set<OsiAttachmentsDTO> osiAttachmentses,
			Set<OsiRespUserDTO> osiRespUsers, Set<OsiUserOperationExclDTO> osiUserOperationExcls*/) {
    super();
    this.id = id;
    this.userName = userName;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailId = emailId;
    this.empNumber = empNumber;
    this.mobileNumber = mobileNumber;
    this.startDate = startDate;
    this.fullName = fullName;
    this.endDate = endDate;
    this.createdBy = createdBy;
    this.createdDate = createdDate;
    this.updatedBy = updatedBy;
    this.updatedDate = updatedDate;
    /*this.osiUserLoginses = osiUserLoginses;
    this.osiUserFuncExcls = osiUserFuncExcls;
    this.osiAttachmentses = osiAttachmentses;
    this.osiRespUsers = osiRespUsers;
    this.osiUserOperationExcls = osiUserOperationExcls;*/
  }

  public String getMenuTree() {
    return menuTree;
  }

  public void setMenuTree(String menuTree) {
    this.menuTree = menuTree;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Integer getBusinessGroupId() {
    return businessGroupId;
  }

  public void setBusinessGroupId(Integer businessGroupId) {
    this.businessGroupId = businessGroupId;
  }

  public List<String> getAdminEmailIds() {
    return adminEmailIds;
  }

  public void setAdminEmailIds(List<String> adminEmailIds) {
    this.adminEmailIds = adminEmailIds;
  }

  public Integer getHasDefaultPwd() {
    return hasDefaultPwd;
  }

  public void setHasDefaultPwd(Integer hasDefaultPwd) {
    this.hasDefaultPwd = hasDefaultPwd;
  }

  public String getHostOrIp() {
    return hostOrIp;
  }

  public void setHostOrIp(String hostOrIp) {
    this.hostOrIp = hostOrIp;
  }

  public boolean isManager() {
    return isManager;
  }

  public void setManager(boolean isManager) {
    this.isManager = isManager;
  }

  public Integer getOrgId() {
    return orgId;
  }

  public void setOrgId(Integer orgId) {
    this.orgId = orgId;
  }

  public String getOrgCode() {
    return orgCode;
  }

  public void setOrgCode(String orgCode) {
    this.orgCode = orgCode;
  }

  public String getPhotoPath() {
    return photoPath;
  }

  public void setPhotoPath(String photoPath) {
    this.photoPath = photoPath;
  }

  public boolean isSkillAdded() {
    return isSkillAdded;
  }

  public void setSkillAdded(boolean isSkillAdded) {
    this.isSkillAdded = isSkillAdded;
  }
}

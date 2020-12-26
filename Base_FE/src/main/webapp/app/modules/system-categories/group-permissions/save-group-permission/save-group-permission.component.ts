import {Component, OnInit} from '@angular/core';
import {HeightService} from "app/shared/services/height.service";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbActiveModal, NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {NgxSpinnerService} from "ngx-spinner";
import {TranslateService} from "@ngx-translate/core";
import {CommonService} from "app/shared/services/common.service";
import {ToastService} from "app/shared/services/toast.service";
import {JhiEventManager} from "ng-jhipster";
import {REGEX_PATTERN} from "app/shared/constants/pattern.constants";
import {SysUserService} from "app/core/services/system-management/sys-user.service";
import {GroupPermissionService} from "app/core/services/group-permission/group-permission.service";
import {TreeviewConfig, TreeviewItem} from 'ngx-treeview';
import {Permission} from "app/core/models/group-permission/permission";
import {enableRipple} from '@syncfusion/ej2-base';
import {ActivatedRoute, Router} from "@angular/router";

enableRipple(true);

@Component({
  selector: 'jhi-save-group-permission',
  templateUrl: './save-group-permission.component.html',
  styleUrls: ['./save-group-permission.component.scss']
})
export class SaveGroupPermissionComponent implements OnInit {
  type: string;
  data: any;
  idGroupPermission: any;
  listRole: any[];
  ngbModalRef: NgbModalRef;
  form: FormGroup;
  height: number;
  statusList: any[] = [{name: 'Hoạt động', value: 'ACTIVE'}, {name: 'Không hoạt động', value: 'INACTIVE'}];
  permissionAll: Permission[];
  dropdownEnabled = true;
  items: TreeviewItem[] = [];
  checkedTreeView: any[] = [];
  checkedTreeViewClone: any[] = [];
  checkIdOld: any[] = [];
  values: number[];
  listNew: any[] = [];
  listOld: any[] = [];
  lstValueNew: any[] = [];
  lstvalueOld: any[] = [];
  dataAdd: any;
  lstHistory: any[] = [];
  config = TreeviewConfig.create({
    hasAllCheckBox: true,
    hasFilter: false,
    hasCollapseExpand: true,
    decoupleChildFromParent: false,
    maxHeight: 300
  });

  constructor(
    public activeModal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private commonService: CommonService,
    private toastService: ToastService,
    private modalService: NgbModal,
    private spinner: NgxSpinnerService,
    private eventManager: JhiEventManager,
    private heightService: HeightService,
    private sysUserService: SysUserService,
    private translateService: TranslateService,
    private groupPermissionService: GroupPermissionService,
    private activatedRoute: ActivatedRoute,
    private routing: Router
  ) {
    this.activatedRoute.queryParams.subscribe(res => {
      this.idGroupPermission = res.id
    })
    this.activatedRoute.params.subscribe(
      res => {
        this.type = res.type;
      })
    this.height = this.heightService.onResizeHeight20Px();
  }

  get formControl() {
    return this.form.controls;
  }

  ngOnInit() {
    this.getAllPermission();
    this.buildForm();
    this.getListRole();
  }

  transition() {
    this.routing.navigate(['/system-categories/group-permissions/' + this.type], {
      queryParams: {
        id: this.idGroupPermission
      }
    });
    this.setDataDefault();
  }

  getDataGrouPermission(id) {
    this.groupPermissionService.findGrouPermissionById(id).subscribe(
      next => {
        const d: any = next;
        this.data = d.data;
        this.form.patchValue(this.data);
        console.warn(93, "data", this.data)
      },
      error => {

      }
    );
  }

  setDataDefault() {
    this.getDataGrouPermission(this.idGroupPermission);
    this.groupPermissionService.getHistoryEdit(this.idGroupPermission).subscribe(
      res => {
        this.lstHistory = res.body;
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
    this.checkedTreeViewClone = [...this.checkedTreeView];
  }

  closeModal() {
    this.routing.navigate(['/system-categories/group-permissions']);
  }

  onSubmitData() {
    this.setValueToField('permissionList', this.getPermissionForFormSubmid());
    if (this.form.invalid) {
      this.commonService.validateAllFormFields(this.form);
      return;
    }
    this.permanentChange();
    this.listChange();
    this.form.value.lstValueNew = JSON.stringify(this.lstValueNew);
    this.form.value.lstValueOld = JSON.stringify(this.lstvalueOld);
    const apiCall = this.type === 'add' ? this.groupPermissionService.save(this.form.value) : this.groupPermissionService.update(this.form.value);

    if (this.type === 'update') {
      this.data.permissionList = this.data.permissionList.map(e => {
        return e.permissionId;
      });
      if (
        this.form.value.id === this.data.id &&
        this.form.value.code === this.data.code &&
        this.form.value.name === this.data.name &&
        this.form.value.status === this.data.status &&
        this.form.value.note === this.data.note &&
        JSON.stringify(this.checkedTreeView) === JSON.stringify(this.data.permissionList)
      ) {
        this.spinner.hide();
        this.transition();
        return;
      }
    }
    apiCall.subscribe(
      res => {
        this.spinner.hide();
        this.type === 'add'
          ? this.toastService.openSuccessToast("Thêm thông tin thành công")
          : this.toastService.openSuccessToast("Cập nhật thông tin thành công");
        this.closeModal();
      },
      err => {
        let msg;
        if (err.error.code === 'EGP010') {
          msg = 'Nhóm quyền đã tồn tại, không thể thêm';
        } else if (err.error.code === 'EGP003') {
          msg = 'Lỗi hệ thống, nhóm quyền chưa được lưu';
        } else if (err.error.code === 'BK000') {
          msg = 'Lỗi valid dữ liệu';
        } else {
          msg = 'Lỗi hệ thống, danh sách quyền chưa được lưu';
        }
        this.spinner.hide();
        this.toastService.openErrorToast(
          msg,
          this.translateService.instant('common.toastr.title.error')
        );
      }
    );

  }

  getPermissionForFormSubmid(): any[] {
    return this.checkedTreeView.map(e => {
      return {permissionId: "" + e}
    });
  }

  customSearchFn(term: string, item: any) {
    const replacedKey = term.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    const newRegEx = new RegExp(replacedKey, 'gi');
    const purgedPosition = item.name.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    return newRegEx.test(purgedPosition);
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  onClearPosition() {
    this.setValueToField('status', 'ACTIVE');
  }

  getValueOfField(item) {
    return this.form.get(item).value;
  }

  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }

  onResize() {
    this.height = this.heightService.onResizeHeight20Px();
  }

  trimSpace(element) {
    const value = this.getValueOfField(element);
    if (value) {
      this.setValueToField(element, value.trim());
    }
  }

  onCancel() {
    if (this.type === 'update') {
      this.data.permissionList = this.data.permissionList.map(e => {
        return e.permissionId;
      });
      if (
        this.form.value.id === this.data.id &&
        this.form.value.code === this.data.code &&
        this.form.value.name === this.data.name &&
        this.form.value.status === this.data.status &&
        this.form.value.note === this.data.note &&
        JSON.stringify(this.checkedTreeView) === JSON.stringify(this.data.permissionList)
      ) {
        this.closeModal();
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.closeModal();
          }
        });
      }
    }
    if (this.type === 'add') {
      console.warn(259, 'form data', this.form.value);
      console.warn(260, 'List data', JSON.stringify(this.checkedTreeView));
      if (
        this.form.value.id === '' &&
        this.form.value.code === '' &&
        this.form.value.name === '' &&
        this.form.value.status === 'ACTIVE' &&
        this.form.value.note === '' &&
        JSON.stringify(this.checkedTreeView) === "[]"
      ) {
        this.closeModal();
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.closeModal();
          }
        });
      }
    }
  }

  getAllPermission() {
    const id = this.idGroupPermission ? this.idGroupPermission : null;
    this.groupPermissionService
      .findAllPermission({id: id})
      .subscribe(
        res => {
          const d: any = res;
          const objTree: any = d.data;
          this.dataAdd = d.data;
          for (let i = 0; i < objTree.length; i++) {
            this.items.push(new TreeviewItem(objTree[i]));
          }
          this.getChecked(this.items, this.checkIdOld);
          this.checkedTreeViewClone = [...this.checkedTreeView];
        },
        err => {
        }
      )
  }

  getChecked(treeView: TreeviewItem[], result: any[]) {
    treeView.forEach(tree => {

      if (tree.checked) {
        result.push(tree.value);
      } else {
        if (tree.indeterminate) {
          result.push(tree.value);
        }
      }
      if (tree.children) {
        this.getChecked(tree.children, result);
      }
    });
    this.checkedTreeView = this.checkIdOld;
  }

  getRoleById(treeView: TreeviewItem[], result) {
    treeView.forEach(tree => {

      if (tree.value === result) {
        return this.lstValueNew.push(tree.text);
      }
      if (tree.children) {
        this.getRoleById(tree.children, result);
      }
    });
  }

  getRoleOldById(treeView: TreeviewItem[], result) {
    treeView.forEach(tree => {

      if (tree.value === result) {
        return this.lstvalueOld.push(tree.text);
      }
      if (tree.children) {
        this.getRoleOldById(tree.children, result);
      }
    });
  }

  onSelectedChange(env) {
    this.checkIdOld = [];
    this.getChecked(this.items, this.checkIdOld);
  }

  listChange() {
    this.listNew = this.checkedTreeView.filter(item => this.checkedTreeViewClone.indexOf(item) < 0);
    this.listOld = this.checkedTreeViewClone.filter(item => this.checkedTreeView.indexOf(item) < 0);
    for (let i = 0; i < this.listNew.length; i++) {
      this.getRoleById(this.dataAdd, this.listNew[i]);
    }
    for (let i = 0; i < this.listOld.length; i++) {
      this.getRoleOldById(this.dataAdd, this.listOld[i]);
    }
  }

  permanentChange() {
    if (this.type === 'update') {
      this.lstValueNew = [];
      this.lstvalueOld = [];
      const itemNew: any = {};
      const itemOld: any = {};
      let change = false;
      if (this.data !== null) {
        if (this.form.value.name !== this.data.name) {
          itemNew.name = this.form.value.name;
          itemOld.name = this.data.name;
          change = true;
        }
        if (this.form.value.status !== this.data.status) {
          itemNew.status = this.form.value.status;
          itemOld.status = this.data.status;
          change = true;
        }
        if (this.form.value.note !== this.data.note) {
          itemNew.note = this.form.value.note;
          itemOld.note = this.data.note;
          change = true;
        }

        this.lstValueNew.push(JSON.stringify(itemNew));
        this.lstvalueOld.push(JSON.stringify(itemOld));
      }
    }
  }

  convertDate(str) {
    if (str === null || str === '') {
      return "";
    } else {
      const date = new Date(str);
      return (date.getDate() < 10 ? ('0' +
        date.getDate()) : (date.getDate())) +
        '/' +
        (date.getMonth() < 9 ? ('0' +
          (date.getMonth() + 1)) : (date.getMonth() + 1)) +
        '/' +
        date.getFullYear();
      // return [date.getFullYear(), mnth, day].join('-');
    }
  }

  viewChangeStr(history) {
    const valueNew = JSON.parse(history.valueNew);
    const valueOld = JSON.parse(history.valueOld);
    const infomationNew = JSON.parse(valueNew[0]);
    const infomationOld = JSON.parse(valueOld[0]);
    let result = "";
    if (infomationNew.name || infomationOld.name) {
      result += 'Tên nhóm quyền được thay đổi: [' + infomationOld.name + '] thành [' + infomationNew.name + ']';
    }
    if (infomationNew.status || infomationOld.status) {
      result += ((result ? '; ' : '') + 'Trạng thái: ' + (infomationOld.status === "ACTIVE" ? '[Hoạt động]' : '[Không hoạt động]') + ' thành ' + (infomationNew.status === "ACTIVE" ? '[Hoạt động]' : '[Không hoạt động]'));
    }
    if (infomationNew.note || infomationOld.note) {
      result += ((result ? '; ' : '') + 'Ghi chú: [' + infomationOld.note + '] thành [' + infomationNew.note + ']');
    }
    let roleAdd = "";
    for (let i = 1; i < valueNew.length; i++) {
      roleAdd += (roleAdd ? ', ' : '') + valueNew[i];
    }
    let roleDelete = "";
    for (let i = 1; i < valueOld.length; i++) {
      roleDelete += (roleDelete ? ', ' : '') + valueOld[i];
    }

    result = result + (result.length > 6 ? '; ' : '') + (roleAdd.length > 1 ? '[Thêm mới các quyền]: ' : '') + roleAdd + (roleAdd.length > 1 && roleDelete.length > 1 ? ', ' : '') + (roleDelete.length > 1 ? '[Xóa các quyền]: ' : '') + roleDelete;

    return result;
  }

  private getListRole() {
    this.groupPermissionService.findListRole().subscribe(
      res => {
        const d: any = res;
        this.listRole = d.data;
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )

  }

  private buildForm() {
    this.form = this.formBuilder.group({
      id: '',
      code: ['', Validators.required],
      name: ['', Validators.compose([Validators.required, Validators.maxLength(255)])],
      status: ['ACTIVE'],
      note: ['', Validators.maxLength(500)],
      permissionList: [[]]
    });
    if (this.idGroupPermission) {
      this.setDataDefault();
    }
  }


}

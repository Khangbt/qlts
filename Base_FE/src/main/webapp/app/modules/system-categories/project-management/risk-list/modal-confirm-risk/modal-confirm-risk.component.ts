import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {RiskListService} from "app/core/services/risk-list/risk-list.service";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {ToastService} from "app/shared/services/toast.service";
import {Router} from "@angular/router";

@Component({
  selector: 'jhi-modal-confirm-risk',
  templateUrl: './modal-confirm-risk.component.html',
  styleUrls: ['./modal-confirm-risk.component.scss']
})
export class ModalConfirmRiskComponent implements OnInit {
  @Input() status;
  @Input() data;
  reason = '';
  count = 0;
  constructor(
    public activeModal: NgbActiveModal,
    public riskListService: RiskListService,
    public  modalService: NgbModal,
    private toastService: ToastService,
    private router: Router
  ) {
  }

  ngOnInit() {
  }

  confirm() {
    if (this.status === 2) {
      if (this.reason.trim() === '' ){
        this.count++;
        if(this.count>4){
          this.toastService.openErrorToast("Bạn phải nhập vào lý do");
          return;
        }
        this.toastService.openWarningToast("Bạn chưa nhập vào lý do");
        return;
      }
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'declineRisk';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.data.fixedStatus = 'Từ chối xác nhận khắc phục';
          this.data.statusConfirm = this.status;
          this.data.reason = this.reason;
          this.riskListService.confirmRiskStatus(this.data).subscribe(success => {
            this.activeModal.dismiss();
            this.toastService.openSuccessToast("Từ chối xác nhận khắc phục thành công");
            this.router.navigate(['/system-categories/project-management/risk-list']);
          });
        }
      });
    }
    if (this.status === 1) {
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'acceptRisk';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.data.fixedStatus = 'Xác nhận đã được khắc phục';
          this.data.statusConfirm = this.status;
          this.data.reason = this.reason;
          this.riskListService.confirmRiskStatus(this.data).subscribe(success => {
            this.activeModal.dismiss();
            this.toastService.openSuccessToast("Xác nhận đã được khắc phục thành công");
            this.router.navigate(['/system-categories/project-management/risk-list']);
          });
        }
      });
    }
  }

  onClose() {
    this.activeModal.dismiss();
  }
}

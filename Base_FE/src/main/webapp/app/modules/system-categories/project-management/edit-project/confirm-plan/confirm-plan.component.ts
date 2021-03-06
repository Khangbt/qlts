import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {HeightService} from "app/shared/services/height.service";
import {ToastService} from "app/shared/services/toast.service";
import {OrganizationCategoriesService} from "app/core/services/system-management/organization-categories.service";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";

@Component({
  selector: 'jhi-confirm-plan',
  templateUrl: './confirm-plan.component.html',
  styleUrls: ['./confirm-plan.component.scss']
})
export class ConfirmPlanComponent implements OnInit {

  @Input() model;
  @Input() type;
  @Output() recorded = new EventEmitter<any>();
  // @Output() recordednote = new EventEmitter<string>();;
  // @Output() status = new EventEmitter<number>();;
  isModalConfirmShow = false;
  height: number;
  reason: string;
  note: string;

  constructor(
    private modalService: NgbModal,
    public activeModal: NgbActiveModal,
    private heightService: HeightService,
    private toastService: ToastService,
    private organizationCategoriesService: OrganizationCategoriesService,
  ) {

  }


  ngOnInit() {
    this.onResize();
    this.loadAll();
  }

  loadAll() {
    if (this.type === 1) {
      this.reason = this.model.reasonUnapproved;
      this.note = this.model.noteUnapproved;
    }
    if (this.type === 2) {
      this.reason = this.model.reasonPrelimiinary;
      this.note = this.model.notePreliinary;
    }
    if (this.type === 3) {
      this.reason = this.model.reasonInternal;
      this.note = this.model.noteInternal;
    }
    if (this.type === 4) {
      this.reason = this.model.reasonOffer;
      this.note = this.model.noteOffer;
    }
    if (this.type === 5) {
      this.reason = this.model.reasonLatch;
      this.note = this.model.noteLatch;
    }
  }

  onCloseAddModal() {
    if (this.reason === null && this.note === null) {
      this.activeModal.dismiss();
    } else {
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'confirm';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.activeModal.dismiss();
        }
      });
    }
  }

  confirm(reason: string, note: string, c: number) {
    if ((reason === null || reason === '') && c === 2) {
      this.toastService.openWarningToast("Cần điền lý do từ chối");
    } else {
      this.recorded.emit({reason: reason, note: note, status: c});
      if (this.type === 1) {
        this.model.noteUnapproved = note;
        this.model.reasonUnapproved = reason;
        this.model.statusUnapproved = c;
        this.model.type = 1;
      }
      if (this.type === 2) {
        this.model.notePreliinary = note;
        this.model.reasonPrelimiinary = reason;
        this.model.statusPreliinary = c;
        this.model.type = 2;
      }
      if (this.type === 3) {
        this.model.noteInternal = note;
        this.model.reasonInternal = reason;
        this.model.statusInternal = c;
        this.model.type = 3;
      }
      if (this.type === 4) {
        this.model.noteOffer = note;
        this.model.reasonOffer = reason;
        this.model.statusOffer = c;
        this.model.type = 4;
      }
      if (this.type === 5) {
        this.model.noteLatch = note;
        this.model.reasonLatch = reason;
        this.model.statusLatch = c;
        this.model.type = 5;
      }
      this.organizationCategoriesService.saveEstimate(this.model).subscribe(data => {

      })
      this.activeModal.dismiss();
    }
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

}

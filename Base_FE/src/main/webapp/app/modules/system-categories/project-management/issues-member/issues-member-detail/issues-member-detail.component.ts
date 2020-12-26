import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal, NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable, of, Subject} from "rxjs";
import {CommonService} from "app/shared/services/common.service";
import {ToastService} from "app/shared/services/toast.service";
import {NgxSpinnerService} from "ngx-spinner";
import {JhiEventManager} from "ng-jhipster";
import {HeightService} from "app/shared/services/height.service";
import {IssuesMemberModel} from "app/core/models/issues-member/issues-member.model";

@Component({
  selector: 'jhi-issues-member-detail',
  templateUrl: './issues-member-detail.component.html',
  styleUrls: ['./issues-member-detail.component.scss']
})
export class IssuesMemberDetailComponent implements OnInit {
  @Input() data:IssuesMemberModel;
  @Input() statusList :any[];
  @Input() listPriority:any[];
  @Input() listSeverity:any[];
  @Output() passEntry:EventEmitter<any> = new EventEmitter()
  ngbModalRef: NgbModalRef;
  form: FormGroup;
  height: number;
  constructor(
    public activeModal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private commonService: CommonService,
    private toastService: ToastService,
    private modalService: NgbModal,
    private spinner: NgxSpinnerService,
    private eventManager: JhiEventManager,
    private heightService: HeightService,
  ) {
    this.height = this.heightService.onResize();
  }

  ngOnInit() {
    this.buildForm();
  }

  private buildForm() {
    this.form = this.formBuilder.group({
      issuesNo:[null],
      codeProject:[null],
      pm:[null],
      author:[null],
      assignee:[null],
      createdOn:[null],
      dueDate:[null],
      priority:[null],
      severity:[null],
      actualProcessingTime:[null],
      status:[null],
      problem:[null],
      description:[null],
      reason:[null],
      solution:[null],
      lessonsLearn:[null]
    });
    this.form.patchValue(this.data);
  }

  get formControl() {
    return this.form.controls;
  }

  onResize() {
    this.height = this.heightService.onResize();
  }

  onCancel() {
    this.activeModal.dismiss(false);
  }

}

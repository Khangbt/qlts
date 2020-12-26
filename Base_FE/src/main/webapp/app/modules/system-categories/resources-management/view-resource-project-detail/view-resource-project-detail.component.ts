import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HumanResourcesApiService } from 'app/core/services/Human-resources-api/human-resources-api.service';
import { ITEMS_PER_PAGE, MAX_SIZE_PAGE } from 'app/shared/constants/pagination.constants';
import { HeightService } from 'app/shared/services/height.service';
import { ToastService } from 'app/shared/services/toast.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'jhi-view-resource-project-detail',
  templateUrl: './view-resource-project-detail.component.html',
  styleUrls: ['./view-resource-project-detail.component.scss']
})
export class ViewResourceProjectDetailComponent implements OnInit {

  form: FormGroup;
  height: number;
  fullName: string;
  @Input() month: any;
  @Input() year: any;
  @Input() data;
  headerHtml: SafeHtml;
  duringHtml: SafeHtml[];
  @Input() resourceData: [any];
  itemsPerPage: any;
  maxSizePage: any;
  previousPage: any;
  page: any;
  totalItems: any;
  searchForm: any;
  @Input() startDate: Date;
  @Input() endDate: Date;
  @Input() startDateSave: Date;
  @Input() endDateSave: Date;
  days: any[];

  constructor(public activeModal: NgbActiveModal,
    private heightService: HeightService,
    private spinner: NgxSpinnerService,
    private formBuilder: FormBuilder,
    private humanResourcesApiService: HumanResourcesApiService,
    private toastService: ToastService,
    private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.buidForm();
    this.form.controls.fullName.setValue(this.data.fullName);
    this.page = 1;
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.maxSizePage = MAX_SIZE_PAGE;
    this.onSearch('defaultSearch');
    this.onResize();
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  private buidForm() {
    this.form = this.formBuilder.group({
      fullName: [],
    });
  }

  onCancel() {
    this.activeModal.dismiss(false);
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.onSearch('defaultSearch');
    }
  }

  handleSearch(type) {
    this.searchForm = {};
    this.searchForm.page = this.page;
    this.searchForm.pageSize = this.itemsPerPage;
    // this.searchForm.keySearchHuman = this.form.value.keySearchHuman;
    this.searchForm.humanResourceId = this.data.humanResourceId;
    this.searchForm.lstProjectId = this.form.value.lstProjectId;
    if (type === 'defaultSearch') {
      if (!this.form.value.startDate && !this.form.value.endDate) {
        this.searchForm.startDate = this.startDateSave;
        this.startDate = this.startDateSave;
        this.searchForm.endDate = this.endDateSave;
        this.endDate = this.endDateSave;
      } else if (this.form.value.startDate && this.form.value.endDate) {
        const startDate = new Date(this.form.value.startDate);
        startDate.setHours(0, 0, 0, 0);
        this.searchForm.startDate = startDate;
        this.startDate = startDate;
        const endDate = new Date(this.form.value.endDate);
        endDate.setHours(23, 59, 59, 999);
        this.searchForm.endDate = endDate;
        this.endDate = endDate;
      } else if (this.form.value.startDate) {
        const startDate = new Date(this.form.value.startDate);
        startDate.setHours(0, 0, 0, 0);
        this.searchForm.startDate = startDate;
        this.startDate = startDate;
        this.endDate = new Date(startDate.getFullYear(), startDate.getMonth() + 1, 0);
        this.endDate.setHours(23, 59, 59, 999);
        this.searchForm.endDate = this.endDate;
      } else if (this.form.value.endDate) {
        const endDate = new Date(this.form.value.endDate);
        endDate.setHours(23, 59, 59, 999);
        this.searchForm.endDate = endDate;
        this.endDate = endDate;
        this.startDate = new Date(endDate.getFullYear(), endDate.getMonth());
        this.startDate.setHours(0, 0, 0, 0);
        this.searchForm.startDate = this.startDate;
      }
      this.month = this.startDate.getMonth() + 1;
      this.year = this.startDate.getFullYear();
    } else if (type === 'changeMonth') {
      let startDate = new Date(this.year, this.month - 1);
      startDate.setHours(0, 0, 0, 0);
      let endDate = new Date(this.year, this.month, 0);
      endDate.setHours(23, 59, 59, 999);
      if (this.form.value.endDate) {
        if (this.endDate && endDate.valueOf() > this.endDate.valueOf()) {
          endDate = this.endDate;
        }
      } else {
        this.endDate = endDate;
      }
      if (this.form.value.startDate) {
        if (this.startDate && startDate.valueOf() < this.startDate.valueOf()) {
          startDate = this.startDate;
        }
      } else {
        this.startDate = startDate;
      }
      this.searchForm.startDate = startDate;
      this.searchForm.endDate = endDate;
    }
  }

  onSearch(type) {
    this.handleSearch(type);
    this.spinner.show();

  }

  generateDate(year, month) {
    const date = new Date(year, month - 1, 1);
    const days = [];
    while (date.getMonth() === month - 1) {
      days.push({
        date: new Date(date),
        str: date.getDate()
      });
      date.setDate(date.getDate() + 1);
    }
    return days;
  }

  changePageSize(size) {
    this.itemsPerPage = size;
    this.onSearch('defaultSearch');
  }

  nextMonth() {
    if (this.month === 12) {
      this.year++;
      this.month = 1;
    } else {
      this.month++;
    }
    this.onSearch('changeMonth');
  }

  previousMonth() {
    if (this.month === 1) {
      this.year--;
      this.month = 12;
    } else {
      this.month--;
    }
    this.onSearch('changeMonth');
  }

}

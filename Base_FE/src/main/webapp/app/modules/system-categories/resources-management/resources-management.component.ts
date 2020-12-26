import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from "ngx-spinner";
import { FormBuilder, FormGroup } from '@angular/forms';
import { Observable, of, Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
// import { SHOW_HIDE_COL_HEIGHT } from 'app/shared/constants/perfect-scroll-height.constants';
import { TIME_OUT } from 'app/shared/constants/set-timeout.constants';
import { HumanResourcesApiService } from 'app/core/services/Human-resources-api/human-resources-api.service';
import { ProjectManagementService } from 'app/core/services/project-management/project-management.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ToastService } from 'app/shared/services/toast.service';
import { ITEMS_PER_PAGE, MAX_SIZE_PAGE } from 'app/shared/constants/pagination.constants';
import { HeightService } from 'app/shared/services/height.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ViewResourceProjectDetailComponent } from './view-resource-project-detail/view-resource-project-detail.component';

// import { SHOW_HIDE_COL_HEIGHT } from 'app/shared/constants/perfect-scroll-height.constants';

@Component({
  selector: 'jhi-resources-management',
  templateUrl: './resources-management.component.html',
  styleUrls: ['./resources-management.component.scss']
})
export class ResourcesManagementComponent implements OnInit {

  form: FormGroup;
  searchHuman;
  searchProject;
  debouncerHuman: Subject<string> = new Subject<string>();
  debouncerProject: Subject<string> = new Subject<string>();
  listHuman = new Observable<any[]>();
  listProject = new Observable<any[]>();
  duringHtml: SafeHtml[] = null;
  headerHtml: SafeHtml;
  resourceData: [any] = null;
  searchForm: any;
  dataSrc: any;
  days: any[];
  startDate: Date;
  endDate: Date;
  startDateSave: Date;
  endDateSave: Date;
  totalItems: any;
  page: any;
  itemsPerPage: any;
  maxSizePage: any;
  previousPage: any;
  month: any;
  year: any;
  height: number;

  constructor(private spinner: NgxSpinnerService,
    private humanResourcesApiService: HumanResourcesApiService,
    private projectManagementService: ProjectManagementService,
    private heightService: HeightService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal,
    private toastService: ToastService,
    private sanitizer: DomSanitizer) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.maxSizePage = MAX_SIZE_PAGE;
  }

  ngOnInit() {
    this.buidForm();
    this.onResize();
    this.debounceOnSearchHuman();
    this.debounceOnSearchProject();
    const d = new Date();
    this.year = d.getFullYear();
    this.month = d.getMonth() + 1;
    this.startDate = new Date(d.getFullYear(), d.getMonth());
    this.startDateSave = this.startDate;
    this.endDate = new Date(d.getFullYear(), d.getMonth() + 1, 0);
    this.endDate.setHours(23, 59, 59, 999);
    this.endDateSave = this.endDate;
    this.onSearch('defaultSearch');
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
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

  handleSearch(type) {
    this.searchForm = {};
    this.searchForm.page = this.page;
    this.searchForm.pageSize = this.itemsPerPage;
    this.searchForm.keySearchHuman = this.form.value.keySearchHuman;
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

  private buidForm() {
    this.form = this.formBuilder.group({
      keySearchHuman: [],
      lstProjectId: [],
      startDate: [null],
      endDate: [null]
    });
  }

  onClearHuman() {
    this.listHuman = of([]);
    this.searchHuman = '';
  }

  onSearchHuman() {
    if (!this.form.value.parentName) {
      this.listHuman = of([]);
      this.searchHuman = '';
    }
  }

  onSearhHuman(event) {
    this.searchHuman = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncerHuman.next(term);
    } else {
      this.listHuman = of([]);
    }
  }

  onSearhProject(event) {
    this.searchProject = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncerProject.next(term);
    } else {
      this.listProject = of([]);
    }
  }

  onSearchProject() {
    if (!this.form.value.parentName) {
      this.listProject = of([]);
      this.searchProject = '';
    }
  }

  onClearProject() {
    this.listProject = of([]);
    this.searchProject = '';
  }

  debounceOnSearchHuman() {
    this.debouncerHuman.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchHuman(value));
  }

  debounceOnSearchProject() {
    this.debouncerProject.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchProject(value));
  }

  loadDataOnSearchHuman(term) {
    const data = {
      keySearch: term.trim().toUpperCase(),
      type: 'PARTNER',
      isSearchByCodeAndFullname: 1
    }
    this.humanResourcesApiService.getHumanResourcesInfo(data).subscribe(res => {
      if (this.searchHuman) {
        const dataRes: any = res;
        this.listHuman = of(dataRes.sort((a, b) => a.fullName.localeCompare(b.fullName)));
      } else {
        this.listHuman = of([]);
      }
    });
  }

  loadDataOnSearchProject(term) {
    const data = {
      keySearch: term.trim().toUpperCase()
    }
    this.projectManagementService.getListProjectByNameOrCode(data).subscribe(res => {
      if (this.searchProject) {
        if (res && res['data']) {
          // const dataRes: any = res;
          const dataRes: any = res['data'];
          this.listProject = of(dataRes.sort((a, b) => a.name.localeCompare(b.name)));
        }
      } else {
        this.listProject = of([]);
      }
    });
  }

  customSearchHunan(term: string, item: any): any {
    term = term.toLocaleLowerCase().trim();
    return (item.fullName ? item.fullName.toLocaleLowerCase().indexOf(term) > -1 : ''.indexOf(term))
      || (item.code ? item.code.toLocaleLowerCase().indexOf(term) > -1 : ''.indexOf(term))
  }

  customSearchProject(term: string, item: any): any {
    term = term.toLocaleLowerCase().trim();
    return (item.name ? item.name.toLocaleLowerCase().indexOf(term) > -1 : ''.indexOf(term))
      || (item.code ? item.code.toLocaleLowerCase().indexOf(term) > -1 : ''.indexOf(term))
  }

  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }

  test(i) {
    alert(i);
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.onSearch('defaultSearch');
    }
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

  // ==============================================

  openModal(item) {
    const modalRef = this.modalService.open(ViewResourceProjectDetailComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.data = item;
    modalRef.componentInstance.month = this.month;
    modalRef.componentInstance.year = this.year;
    // modalRef.componentInstance.headerHtml = this.headerHtml;
    // modalRef.componentInstance.duringHtml = this.duringHtml;
    modalRef.componentInstance.resourceData = this.resourceData;
    modalRef.componentInstance.startDate = this.startDate;
    modalRef.componentInstance.endDate = this.endDate;
    modalRef.componentInstance.startDateSave = this.startDateSave;
    modalRef.componentInstance.endDateSave = this.endDateSave;
  }
}

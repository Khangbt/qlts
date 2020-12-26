import {Directive, ElementRef, Input, OnInit, Output, TemplateRef, ViewContainerRef} from '@angular/core';
import {CommonService} from "app/shared/services/common.service";
import {STORAGE_KEYS} from "app/shared/constants/storage-keys.constants";
import {FormStoringService} from "app/shared/services/form-storing.service";

@Directive({
  selector: '[jhiHasPermission]'
})
export class HasPermissionDirective implements OnInit {
  private _value: any;
  private _value1: any;

  private user: any;

  constructor(
    private element: ElementRef,
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private commonService: CommonService,
    private formStoringService: FormStoringService,
  ) {

  }

  @Input()
  set jhiHasPermission(value) {
    this._value1 = value;
    this.updateViewAll(this._value1)
  }


  private updateViewAll(vale) {
    const userToken: any = this.formStoringService.get(STORAGE_KEYS.USER);
    if (vale === 1) {
      this.viewContainer.createEmbeddedView(this.templateRef);

    } else if (vale === 2) {
      if (userToken.role === 'ROLE_ALL' || userToken.role === 'ROLE_ADMINPART') {
        this.viewContainer.createEmbeddedView(this.templateRef);
      } else {
        this.viewContainer.clear();
      }
    } else if (vale === 3) {
      if (userToken.role === 'ROLE_ALL') {
        this.viewContainer.createEmbeddedView(this.templateRef);
      } else {
        this.viewContainer.clear();
      }
    }
    else if(vale === 4){

      if (userToken.role === 'ROLE_ADMINPART') {
        this.viewContainer.createEmbeddedView(this.templateRef);
      } else {
        this.viewContainer.clear();
      }
    }

    else {
      if(vale.tyleDto==="DEVICE"){
        if(vale.status===1){
          if ((userToken.role === 'ROLE_ALL')){
            this.viewContainer.createEmbeddedView(this.templateRef);
          }
          else if (  userToken.role === 'ROLE_ADMINPART'&& (userToken.partId === vale.partId)) {
            this.viewContainer.createEmbeddedView(this.templateRef);
          } else {
            this.viewContainer.clear();
          }
        }else {
          this.viewContainer.clear();
        }
      }
      if(vale.tyleDto==="REQUEST"){
        if(vale.status===1){
          if (userToken.humanResourceId === vale.creatHummerId){
            this.viewContainer.createEmbeddedView(this.templateRef);
          }
        }else {
          this.viewContainer.clear();
        }
      }
      if(vale.tyleDto==="HUMMER"){
        if (userToken.role === 'ROLE_ALL') {
          this.viewContainer.createEmbeddedView(this.templateRef);
        } else if (userToken.humanResourceId === vale.humanResourceId&&userToken.role === 'ROLE_USER'){
            this.viewContainer.createEmbeddedView(this.templateRef);
        }else {
          if(userToken.role==='ROLE_ADMINPART'&& (userToken.partId === vale.partId)){
            this.viewContainer.createEmbeddedView(this.templateRef);
          }else {
            this.viewContainer.clear();
          }

        }
      }
    }
  }

  private updateView(value) {
    this.viewContainer.createEmbeddedView(this.templateRef);

    // if (this.commonService.havePermission(value[0])) {
    //   this.viewContainer.createEmbeddedView(this.templateRef);
    // } else {
    //   this.viewContainer.clear();
    // }
  }

  ngOnInit(): void {
  }
}

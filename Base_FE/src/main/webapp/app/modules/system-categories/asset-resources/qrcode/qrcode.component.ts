import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {AssetModel} from "app/core/models/asset-model/asset-model";

@Component({
  selector: 'jhi-qrcode',
  templateUrl: './qrcode.component.html',
  styleUrls: ['./qrcode.component.scss']
})
export class QRcodeComponent implements OnInit {
  listAsset:any;
  title = 'app';
  elementType = 'url';
  value = 'Techiediaries';

  codepc = "Mã máy tính: ";
  namepc = ". Tên máy tính: ";
  inforpc: ". Thông tin máy tính: "


  predicate: any;
  reverse: any;
  listAsset1: AssetModel[] = [];
  private printService: any;

  constructor(
    public activeModal: NgbActiveModal,
  ) { }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.listAsset1 = this.listAsset;
  }

  onCancel() {
    this.activeModal.dismiss();
  }
  print(){
    window.print();
    const invoiceIds = ['101', '102'];
    this.printService
      .printDocument('invoice', invoiceIds);
  }


}

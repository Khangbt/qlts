import {Component, Input, OnInit} from '@angular/core';
import {ChartDataSets, ChartOptions, ChartType} from 'chart.js';
import {Color, Label} from 'ng2-charts';
import {OrganizationCategoriesService} from 'app/core/services/system-management/organization-categories.service';
import {ActivatedRoute} from '@angular/router';
import {OrganizationCategoriesModel} from 'app/core/models/system-categories/organization-categories.model';
import ChartDataLabels from 'chartjs-plugin-datalabels';


@Component({
  selector: 'jhi-chart-kpi',
  templateUrl: './chart-kpi.component.html',
  styleUrls: ['./chart-kpi.component.scss']
})
export class ChartKPIComponent implements OnInit {

  @Input() id: any;
  @Input() projectName: string;
  barChartOptions: ChartOptions;
  project: OrganizationCategoriesModel;
  projectId: number;
  barChartLabels: Label[] = ['ULNL NỘI BỘ (MM)', 'NL THỰC TẾ (MM)', 'NL THANH TOÁN (MM)', 'TỔNG NỖ LỰC (MM)'];
  barChartType: ChartType = 'bar';
  barChartLegend = false;
  barChartPlugins = [ChartDataLabels];
  ULSB: number;//so bo
  ULCG: number;// thuc te
  ULTT: number;// chao gia
  ULNC: number;// chot, phe duyet
  ULNB: number;// noi bo
  SumNL: number;
  namePM: string;// ten PM lead
  nameProject: string// ten du an
  barChartData: ChartDataSets[] = [
    {data: [this.ULNB, this.ULCG, this.ULNC, this.SumNL]}
  ];


  barChartColors: Color[] = [
    {backgroundColor: ['#73b0d7', '#a0d771', '#fbad56', '#e45621']},
  ]

  constructor(
    private organizationCategoriesService: OrganizationCategoriesService,
    private activatedRoute: ActivatedRoute
  ) {
    this.activatedRoute.queryParams.subscribe(param => {
      this.projectId = param.id;
    });

  }

  setOptionChart() {
    this.barChartOptions = {
      responsive: true,
      scales: {
        xAxes: [{ticks: {beginAtZero: true}}], yAxes: [{
          ticks: {
            beginAtZero: true, callback: function (value) {
              return value + "MM"
            }
          }
        }]
      },
      plugins: {
        datalabels: {
          anchor: 'center',
          align: 'end',
          display: true,
          font: {
            size: 25,
            weight: 'bold',
          },
          color: "#875A5A",
        }
      },
      title: {
        display: true,
        text: this.projectName ? ('KPI nguồn lực "' + this.projectName + '"- ' +
          'Leader ' + (this.namePM?this.namePM:'') ): '',
        fontSize : 16,
      }
    };
  }

  ngOnInit(): void {
    const dataProject = {
      projectId: this.id,
    }
    this.organizationCategoriesService.getProjectById(dataProject).subscribe(res => {
      this.ULSB = res.data[0].estimatePrelimiinary;
      this.ULCG = res.data[0].estimateActual === 0 ? null : res.data[0].estimateActual;
      this.ULTT = res.data[0].estimateOffer;
      this.ULNC = res.data[0].estimateLatch;
      this.ULNB = res.data[0].estimateInternal;
      this.SumNL = this.ULCG + (0.5 * this.ULCG) === 0 ? null : this.ULCG + (0.5 * this.ULCG);
      if (this.SumNL) {
        this.SumNL = parseFloat(this.SumNL.toFixed(2));
      }
      this.namePM = res.data[0].pmName;
      this.nameProject = res.data[0].name;
      this.barChartData = [
        {data: [this.ULNB, this.ULCG, this.ULNC, this.SumNL]}
      ]
      this.setOptionChart();
    });
  }

}

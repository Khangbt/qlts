import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ResourcesByTimeComponentRoutingModule } from './resources-by-time-component-routing.module';
import { ResourcesByTimeComponentComponent } from './resources-by-time-component.component';


@NgModule({
  declarations: [
    ResourcesByTimeComponentComponent,
  ],
  imports: [
    CommonModule,
    ResourcesByTimeComponentRoutingModule,
  ]
})
export class ResourcesByTimeComponentModule { }

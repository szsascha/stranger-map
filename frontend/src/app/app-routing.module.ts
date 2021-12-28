import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { JoinComponent } from './join/join.component';
import { MapComponent } from './map/map.component';

const routes: Routes = [
  { path: 'join', component: JoinComponent },
  { path: 'map', component: MapComponent },
  { path: '', redirectTo: '/join', pathMatch: 'full' },
  //{ path: '**', component: PageNotFoundComponent },  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

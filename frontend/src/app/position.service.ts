import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PositionService {

  public position$ = new Subject<GeolocationPosition>();

  constructor() { 
    navigator.geolocation.watchPosition(
      (pos) => this.position$.next(pos), 
      (err: any) => this.position$.error(err)
    );
  }

}

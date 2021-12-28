import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PositionService {

  public position$ = new BehaviorSubject<GeolocationPosition | null>(null);

  constructor() { 
    navigator.geolocation.watchPosition(
      (pos) => this.position$.next(pos), 
      (err: any) => this.position$.error(err)
    );
  }

}

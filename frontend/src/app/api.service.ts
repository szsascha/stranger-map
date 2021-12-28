import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

interface JoinRequestDto {
  name: string;
  description: string;
}

interface JoinResponseDto {
   uuid: string;
}

interface Peer {
  name: string;
  description: string;
  lat: number;
  lon: number;
}

interface SubmitPositionDto {
  uuid: string;
  lat: number;
  lon: number;
}

interface PeersDto {
  peers: Peer[];
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

  public session(joinRequestDto: JoinRequestDto): Observable<JoinResponseDto> {
    return this.http.post<JoinResponseDto>('/api/session', joinRequestDto)
  }

  public peers(submitPositionDto: SubmitPositionDto): Observable<PeersDto> {
    return this.http.post<PeersDto>('/api/peers', submitPositionDto)
  }
}

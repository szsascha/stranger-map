import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { JoinRequestDto } from 'src/interfaces/join-request-dto';
import { JoinResponseDto } from 'src/interfaces/join-response-dto';
import { PeersDto } from 'src/interfaces/peers-dto';
import { SubmitPositionDto } from 'src/interfaces/submit-position-dto';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

  public session(joinRequestDto: JoinRequestDto): Observable<JoinResponseDto> {
    return this.http.post<JoinResponseDto>(`${environment.api}/api/session`, joinRequestDto)
  }

  public peers(submitPositionDto: SubmitPositionDto): Observable<PeersDto> {
    return this.http.post<PeersDto>(`${environment.api}/api/peers`, submitPositionDto)
  }
}

import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-join',
  templateUrl: './join.component.html',
  styleUrls: ['./join.component.scss']
})
export class JoinComponent {

  public joinForm;

  constructor(
    private fb: FormBuilder, 
    private api: ApiService,
    private router: Router) { 
    this.joinForm = this.fb.group({
      name: [''],
      description: [''],
    })
  }

  onSubmit(): void {
    console.log(this.joinForm.valid, this.joinForm.value)
    if(this.joinForm.valid) {
      this.api.session(this.joinForm.value).subscribe({
        next: (response) => {
          const uuid = response.uuid
          this.router.navigate(['map', uuid])
        },
        error: () => {
          throw new Error("Error Handling?!?!?!?")
        }
      })
    }
    
  }

}

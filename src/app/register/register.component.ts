import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserCredentials } from '../user-credentials';
import { ReactiveFormsModule, FormControl, FormGroup, Validators} from '@angular/forms';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ ReactiveFormsModule ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  registerForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
  });

  userCredentials: UserCredentials;

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService) {
      this.userCredentials = new UserCredentials();
  }

  onSubmit() {
    this.userCredentials.username = this.registerForm.value.username || '';
    this.userCredentials.password = this.registerForm.value.password || '';

    this.userService.createUser(this.userCredentials).subscribe({
      next: () => this.goToLogin(),
      error: err => console.error('Registration Failed', err)
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { StorageService } from 'src/app/services/storage.service';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: User | null = null;
  profileForm!: FormGroup;
  selectedFile!: File;
  defaultAvatar = '//ssl.gstatic.com/accounts/ui/avatar_2x.png';

  constructor(
    private storageService: StorageService,
    private userService: UserService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    const user = this.storageService.getUser();
    if (user && user.id) {
      this.userService.getUser(user.id).subscribe({
        next: (u) => {
          this.currentUser = u;
          console.log(this.currentUser );
          this.initForm(u);
        },
        error: (err) => console.error(err)
      });
    }
  }

  initForm(user: User) {
    this.profileForm = this.fb.group({
      fullName: [user.fullName || ''],
      phoneNumber: [user.phoneNumber || ''],
      city: [user.city || ''],
      address: [user.address || '']
    });
  }

  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  onSubmit() {
    if (!this.currentUser) return;
    const dto: any = {
      fullName: this.profileForm.value.fullName,
      phoneNumber: this.profileForm.value.phoneNumber,
      city: this.profileForm.value.city,
      address: this.profileForm.value.address
    };

    // Dacă s-a ales un fișier, mai întâi upload
    if (this.selectedFile) {
      this.userService.uploadProfileImage(this.currentUser.id, this.selectedFile)
        .subscribe({
          next: (res) => {
            dto.profileImageUrl = res.profileImageUrl;
            this.saveProfile(dto);
          },
          error: (err) => console.error(err)
        });
    } else {
      this.saveProfile(dto);
    }
  }

  saveProfile(dto: Partial<User>) {
    if (!this.currentUser) return;
    this.userService.updateProfile(this.currentUser.id, dto)
      .subscribe({
        next: (updatedUser) => {
          this.currentUser = updatedUser;
          alert('Profil actualizat cu succes!');
        },
        error: (err) => console.error(err)
      });
  }
}

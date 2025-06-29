export interface User {
  id: number;
  username: string;
  email: string;
  roles: string[];
  profileImageUrl?: string;
  fullName?: string;
  phoneNumber?: string;
  city?: string;
  address?: string;
}

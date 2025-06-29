
export interface GymRoom {
  id: number;
  name: string;
  description?: string;
  address?: string;
  city?: string;
  neighborhood?: string;
  latitude?: number;
  longitude?: number;
  workingHours?: string;
  phoneNumber?: string;
  email?: string;
  imageUrl?: string;
  capacity?: number;
}

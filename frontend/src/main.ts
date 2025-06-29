import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { registerLicense } from '@syncfusion/ej2-base';

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
registerLicense('ORg4AjUWIQA/Gnt2U1hhQlJBfVhdXGVWfFN0QXNfdVxxflFEcDwsT3RfQFljT39Vd01gXXxZd3ZSQw==');

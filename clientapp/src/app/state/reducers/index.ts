import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import settings, { SettingsState } from '../../modules/account/settings/settings.reducer';
import navigationItems, { NavigationItemsState } from './navigation';
import routes, { RouteItemsState } from './routes';
import sidebarItems, { SidebarItemsState } from './sidebar';
import scripts, { ScriptItemsState } from './scripts';

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly settings: SettingsState;
  readonly loadingBar: any;
  readonly navigationItems: NavigationItemsState;
  readonly routes: RouteItemsState;
  readonly sidebarItems: SidebarItemsState;
  readonly scripts: ScriptItemsState;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  settings,
  loadingBar,
  navigationItems,
  routes,
  sidebarItems,
  scripts
});

export default rootReducer;

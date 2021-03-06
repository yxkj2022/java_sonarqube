/*
 * SonarQube
 * Copyright (C) 2009-2019 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
import * as React from 'react';
import { shallow } from 'enzyme';
import ConfigureWithLocalScanner from '../ConfigureWithLocalScanner';
import { TutorialProps } from '../../utils';
import { mockComponent, mockLoggedInUser } from '../../../../../helpers/testMocks';

it('should render correctly', () => {
  expect(shallowRender()).toMatchSnapshot();
});

function shallowRender(props: Partial<TutorialProps> = {}) {
  return shallow(
    <ConfigureWithLocalScanner
      component={mockComponent()}
      currentUser={mockLoggedInUser()}
      onDone={jest.fn()}
      setToken={jest.fn()}
      token="token123"
      {...props}
    />
  );
}

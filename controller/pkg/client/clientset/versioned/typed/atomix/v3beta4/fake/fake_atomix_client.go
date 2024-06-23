// SPDX-FileCopyrightText: 2022-present Intel Corporation
//
// SPDX-License-Identifier: Apache-2.0
// Code generated by client-gen. DO NOT EDIT.

package fake

import (
	v3beta4 "github.com/micro-onos-revamped/atomix/controller/pkg/client/clientset/versioned/typed/atomix/v3beta4"
	rest "k8s.io/client-go/rest"
	testing "k8s.io/client-go/testing"
)

type FakeAtomixV3beta4 struct {
	*testing.Fake
}

func (c *FakeAtomixV3beta4) DataStores(namespace string) v3beta4.DataStoreInterface {
	return &FakeDataStores{c, namespace}
}

func (c *FakeAtomixV3beta4) StorageProfiles(namespace string) v3beta4.StorageProfileInterface {
	return &FakeStorageProfiles{c, namespace}
}

// RESTClient returns a RESTClient that is used to communicate
// with API server by this client implementation.
func (c *FakeAtomixV3beta4) RESTClient() rest.Interface {
	var ret *rest.RESTClient
	return ret
}

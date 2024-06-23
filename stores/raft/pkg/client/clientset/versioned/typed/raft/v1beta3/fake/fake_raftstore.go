// SPDX-FileCopyrightText: 2022-present Intel Corporation
//
// SPDX-License-Identifier: Apache-2.0
// Code generated by client-gen. DO NOT EDIT.

package fake

import (
	"context"

	v1beta3 "github.com/micro-onos-revamped/atomix/stores/raft/pkg/apis/raft/v1beta3"
	v1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	labels "k8s.io/apimachinery/pkg/labels"
	schema "k8s.io/apimachinery/pkg/runtime/schema"
	types "k8s.io/apimachinery/pkg/types"
	watch "k8s.io/apimachinery/pkg/watch"
	testing "k8s.io/client-go/testing"
)

// FakeRaftStores implements RaftStoreInterface
type FakeRaftStores struct {
	Fake *FakeRaftV1beta3
	ns   string
}

var raftstoresResource = schema.GroupVersionResource{Group: "raft.atomix.io", Version: "v1beta3", Resource: "raftstores"}

var raftstoresKind = schema.GroupVersionKind{Group: "raft.atomix.io", Version: "v1beta3", Kind: "RaftStore"}

// Get takes name of the raftStore, and returns the corresponding raftStore object, and an error if there is any.
func (c *FakeRaftStores) Get(ctx context.Context, name string, options v1.GetOptions) (result *v1beta3.RaftStore, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewGetAction(raftstoresResource, c.ns, name), &v1beta3.RaftStore{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta3.RaftStore), err
}

// List takes label and field selectors, and returns the list of RaftStores that match those selectors.
func (c *FakeRaftStores) List(ctx context.Context, opts v1.ListOptions) (result *v1beta3.RaftStoreList, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewListAction(raftstoresResource, raftstoresKind, c.ns, opts), &v1beta3.RaftStoreList{})

	if obj == nil {
		return nil, err
	}

	label, _, _ := testing.ExtractFromListOptions(opts)
	if label == nil {
		label = labels.Everything()
	}
	list := &v1beta3.RaftStoreList{ListMeta: obj.(*v1beta3.RaftStoreList).ListMeta}
	for _, item := range obj.(*v1beta3.RaftStoreList).Items {
		if label.Matches(labels.Set(item.Labels)) {
			list.Items = append(list.Items, item)
		}
	}
	return list, err
}

// Watch returns a watch.Interface that watches the requested raftStores.
func (c *FakeRaftStores) Watch(ctx context.Context, opts v1.ListOptions) (watch.Interface, error) {
	return c.Fake.
		InvokesWatch(testing.NewWatchAction(raftstoresResource, c.ns, opts))

}

// Create takes the representation of a raftStore and creates it.  Returns the server's representation of the raftStore, and an error, if there is any.
func (c *FakeRaftStores) Create(ctx context.Context, raftStore *v1beta3.RaftStore, opts v1.CreateOptions) (result *v1beta3.RaftStore, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewCreateAction(raftstoresResource, c.ns, raftStore), &v1beta3.RaftStore{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta3.RaftStore), err
}

// Update takes the representation of a raftStore and updates it. Returns the server's representation of the raftStore, and an error, if there is any.
func (c *FakeRaftStores) Update(ctx context.Context, raftStore *v1beta3.RaftStore, opts v1.UpdateOptions) (result *v1beta3.RaftStore, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewUpdateAction(raftstoresResource, c.ns, raftStore), &v1beta3.RaftStore{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta3.RaftStore), err
}

// UpdateStatus was generated because the type contains a Status member.
// Add a +genclient:noStatus comment above the type to avoid generating UpdateStatus().
func (c *FakeRaftStores) UpdateStatus(ctx context.Context, raftStore *v1beta3.RaftStore, opts v1.UpdateOptions) (*v1beta3.RaftStore, error) {
	obj, err := c.Fake.
		Invokes(testing.NewUpdateSubresourceAction(raftstoresResource, "status", c.ns, raftStore), &v1beta3.RaftStore{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta3.RaftStore), err
}

// Delete takes name of the raftStore and deletes it. Returns an error if one occurs.
func (c *FakeRaftStores) Delete(ctx context.Context, name string, opts v1.DeleteOptions) error {
	_, err := c.Fake.
		Invokes(testing.NewDeleteActionWithOptions(raftstoresResource, c.ns, name, opts), &v1beta3.RaftStore{})

	return err
}

// DeleteCollection deletes a collection of objects.
func (c *FakeRaftStores) DeleteCollection(ctx context.Context, opts v1.DeleteOptions, listOpts v1.ListOptions) error {
	action := testing.NewDeleteCollectionAction(raftstoresResource, c.ns, listOpts)

	_, err := c.Fake.Invokes(action, &v1beta3.RaftStoreList{})
	return err
}

// Patch applies the patch and returns the patched raftStore.
func (c *FakeRaftStores) Patch(ctx context.Context, name string, pt types.PatchType, data []byte, opts v1.PatchOptions, subresources ...string) (result *v1beta3.RaftStore, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewPatchSubresourceAction(raftstoresResource, c.ns, name, pt, data, subresources...), &v1beta3.RaftStore{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta3.RaftStore), err
}

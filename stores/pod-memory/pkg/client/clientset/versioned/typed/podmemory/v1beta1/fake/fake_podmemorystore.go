// SPDX-FileCopyrightText: 2022-present Intel Corporation
//
// SPDX-License-Identifier: Apache-2.0
// Code generated by client-gen. DO NOT EDIT.

package fake

import (
	"context"

	v1beta1 "github.com/micro-onos-revamped/atomix/stores/pod-memory/pkg/apis/podmemory/v1beta1"
	v1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	labels "k8s.io/apimachinery/pkg/labels"
	schema "k8s.io/apimachinery/pkg/runtime/schema"
	types "k8s.io/apimachinery/pkg/types"
	watch "k8s.io/apimachinery/pkg/watch"
	testing "k8s.io/client-go/testing"
)

// FakePodMemoryStores implements PodMemoryStoreInterface
type FakePodMemoryStores struct {
	Fake *FakePodmemoryV1beta1
	ns   string
}

var podmemorystoresResource = schema.GroupVersionResource{Group: "podmemory.atomix.io", Version: "v1beta1", Resource: "podmemorystores"}

var podmemorystoresKind = schema.GroupVersionKind{Group: "podmemory.atomix.io", Version: "v1beta1", Kind: "PodMemoryStore"}

// Get takes name of the podMemoryStore, and returns the corresponding podMemoryStore object, and an error if there is any.
func (c *FakePodMemoryStores) Get(ctx context.Context, name string, options v1.GetOptions) (result *v1beta1.PodMemoryStore, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewGetAction(podmemorystoresResource, c.ns, name), &v1beta1.PodMemoryStore{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.PodMemoryStore), err
}

// List takes label and field selectors, and returns the list of PodMemoryStores that match those selectors.
func (c *FakePodMemoryStores) List(ctx context.Context, opts v1.ListOptions) (result *v1beta1.PodMemoryStoreList, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewListAction(podmemorystoresResource, podmemorystoresKind, c.ns, opts), &v1beta1.PodMemoryStoreList{})

	if obj == nil {
		return nil, err
	}

	label, _, _ := testing.ExtractFromListOptions(opts)
	if label == nil {
		label = labels.Everything()
	}
	list := &v1beta1.PodMemoryStoreList{ListMeta: obj.(*v1beta1.PodMemoryStoreList).ListMeta}
	for _, item := range obj.(*v1beta1.PodMemoryStoreList).Items {
		if label.Matches(labels.Set(item.Labels)) {
			list.Items = append(list.Items, item)
		}
	}
	return list, err
}

// Watch returns a watch.Interface that watches the requested podMemoryStores.
func (c *FakePodMemoryStores) Watch(ctx context.Context, opts v1.ListOptions) (watch.Interface, error) {
	return c.Fake.
		InvokesWatch(testing.NewWatchAction(podmemorystoresResource, c.ns, opts))

}

// Create takes the representation of a podMemoryStore and creates it.  Returns the server's representation of the podMemoryStore, and an error, if there is any.
func (c *FakePodMemoryStores) Create(ctx context.Context, podMemoryStore *v1beta1.PodMemoryStore, opts v1.CreateOptions) (result *v1beta1.PodMemoryStore, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewCreateAction(podmemorystoresResource, c.ns, podMemoryStore), &v1beta1.PodMemoryStore{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.PodMemoryStore), err
}

// Update takes the representation of a podMemoryStore and updates it. Returns the server's representation of the podMemoryStore, and an error, if there is any.
func (c *FakePodMemoryStores) Update(ctx context.Context, podMemoryStore *v1beta1.PodMemoryStore, opts v1.UpdateOptions) (result *v1beta1.PodMemoryStore, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewUpdateAction(podmemorystoresResource, c.ns, podMemoryStore), &v1beta1.PodMemoryStore{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.PodMemoryStore), err
}

// UpdateStatus was generated because the type contains a Status member.
// Add a +genclient:noStatus comment above the type to avoid generating UpdateStatus().
func (c *FakePodMemoryStores) UpdateStatus(ctx context.Context, podMemoryStore *v1beta1.PodMemoryStore, opts v1.UpdateOptions) (*v1beta1.PodMemoryStore, error) {
	obj, err := c.Fake.
		Invokes(testing.NewUpdateSubresourceAction(podmemorystoresResource, "status", c.ns, podMemoryStore), &v1beta1.PodMemoryStore{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.PodMemoryStore), err
}

// Delete takes name of the podMemoryStore and deletes it. Returns an error if one occurs.
func (c *FakePodMemoryStores) Delete(ctx context.Context, name string, opts v1.DeleteOptions) error {
	_, err := c.Fake.
		Invokes(testing.NewDeleteActionWithOptions(podmemorystoresResource, c.ns, name, opts), &v1beta1.PodMemoryStore{})

	return err
}

// DeleteCollection deletes a collection of objects.
func (c *FakePodMemoryStores) DeleteCollection(ctx context.Context, opts v1.DeleteOptions, listOpts v1.ListOptions) error {
	action := testing.NewDeleteCollectionAction(podmemorystoresResource, c.ns, listOpts)

	_, err := c.Fake.Invokes(action, &v1beta1.PodMemoryStoreList{})
	return err
}

// Patch applies the patch and returns the patched podMemoryStore.
func (c *FakePodMemoryStores) Patch(ctx context.Context, name string, pt types.PatchType, data []byte, opts v1.PatchOptions, subresources ...string) (result *v1beta1.PodMemoryStore, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewPatchSubresourceAction(podmemorystoresResource, c.ns, name, pt, data, subresources...), &v1beta1.PodMemoryStore{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.PodMemoryStore), err
}

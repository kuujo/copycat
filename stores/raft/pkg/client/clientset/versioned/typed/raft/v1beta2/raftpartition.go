// SPDX-FileCopyrightText: 2022-present Intel Corporation
//
// SPDX-License-Identifier: Apache-2.0
// Code generated by client-gen. DO NOT EDIT.

package v1beta2

import (
	"context"
	"time"

	v1beta2 "github.com/micro-onos-revamped/atomix/stores/raft/pkg/apis/raft/v1beta2"
	scheme "github.com/micro-onos-revamped/atomix/stores/raft/pkg/client/clientset/versioned/scheme"
	v1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	types "k8s.io/apimachinery/pkg/types"
	watch "k8s.io/apimachinery/pkg/watch"
	rest "k8s.io/client-go/rest"
)

// RaftPartitionsGetter has a method to return a RaftPartitionInterface.
// A group's client should implement this interface.
type RaftPartitionsGetter interface {
	RaftPartitions(namespace string) RaftPartitionInterface
}

// RaftPartitionInterface has methods to work with RaftPartition resources.
type RaftPartitionInterface interface {
	Create(ctx context.Context, raftPartition *v1beta2.RaftPartition, opts v1.CreateOptions) (*v1beta2.RaftPartition, error)
	Update(ctx context.Context, raftPartition *v1beta2.RaftPartition, opts v1.UpdateOptions) (*v1beta2.RaftPartition, error)
	UpdateStatus(ctx context.Context, raftPartition *v1beta2.RaftPartition, opts v1.UpdateOptions) (*v1beta2.RaftPartition, error)
	Delete(ctx context.Context, name string, opts v1.DeleteOptions) error
	DeleteCollection(ctx context.Context, opts v1.DeleteOptions, listOpts v1.ListOptions) error
	Get(ctx context.Context, name string, opts v1.GetOptions) (*v1beta2.RaftPartition, error)
	List(ctx context.Context, opts v1.ListOptions) (*v1beta2.RaftPartitionList, error)
	Watch(ctx context.Context, opts v1.ListOptions) (watch.Interface, error)
	Patch(ctx context.Context, name string, pt types.PatchType, data []byte, opts v1.PatchOptions, subresources ...string) (result *v1beta2.RaftPartition, err error)
	RaftPartitionExpansion
}

// raftPartitions implements RaftPartitionInterface
type raftPartitions struct {
	client rest.Interface
	ns     string
}

// newRaftPartitions returns a RaftPartitions
func newRaftPartitions(c *RaftV1beta2Client, namespace string) *raftPartitions {
	return &raftPartitions{
		client: c.RESTClient(),
		ns:     namespace,
	}
}

// Get takes name of the raftPartition, and returns the corresponding raftPartition object, and an error if there is any.
func (c *raftPartitions) Get(ctx context.Context, name string, options v1.GetOptions) (result *v1beta2.RaftPartition, err error) {
	result = &v1beta2.RaftPartition{}
	err = c.client.Get().
		Namespace(c.ns).
		Resource("raftpartitions").
		Name(name).
		VersionedParams(&options, scheme.ParameterCodec).
		Do(ctx).
		Into(result)
	return
}

// List takes label and field selectors, and returns the list of RaftPartitions that match those selectors.
func (c *raftPartitions) List(ctx context.Context, opts v1.ListOptions) (result *v1beta2.RaftPartitionList, err error) {
	var timeout time.Duration
	if opts.TimeoutSeconds != nil {
		timeout = time.Duration(*opts.TimeoutSeconds) * time.Second
	}
	result = &v1beta2.RaftPartitionList{}
	err = c.client.Get().
		Namespace(c.ns).
		Resource("raftpartitions").
		VersionedParams(&opts, scheme.ParameterCodec).
		Timeout(timeout).
		Do(ctx).
		Into(result)
	return
}

// Watch returns a watch.Interface that watches the requested raftPartitions.
func (c *raftPartitions) Watch(ctx context.Context, opts v1.ListOptions) (watch.Interface, error) {
	var timeout time.Duration
	if opts.TimeoutSeconds != nil {
		timeout = time.Duration(*opts.TimeoutSeconds) * time.Second
	}
	opts.Watch = true
	return c.client.Get().
		Namespace(c.ns).
		Resource("raftpartitions").
		VersionedParams(&opts, scheme.ParameterCodec).
		Timeout(timeout).
		Watch(ctx)
}

// Create takes the representation of a raftPartition and creates it.  Returns the server's representation of the raftPartition, and an error, if there is any.
func (c *raftPartitions) Create(ctx context.Context, raftPartition *v1beta2.RaftPartition, opts v1.CreateOptions) (result *v1beta2.RaftPartition, err error) {
	result = &v1beta2.RaftPartition{}
	err = c.client.Post().
		Namespace(c.ns).
		Resource("raftpartitions").
		VersionedParams(&opts, scheme.ParameterCodec).
		Body(raftPartition).
		Do(ctx).
		Into(result)
	return
}

// Update takes the representation of a raftPartition and updates it. Returns the server's representation of the raftPartition, and an error, if there is any.
func (c *raftPartitions) Update(ctx context.Context, raftPartition *v1beta2.RaftPartition, opts v1.UpdateOptions) (result *v1beta2.RaftPartition, err error) {
	result = &v1beta2.RaftPartition{}
	err = c.client.Put().
		Namespace(c.ns).
		Resource("raftpartitions").
		Name(raftPartition.Name).
		VersionedParams(&opts, scheme.ParameterCodec).
		Body(raftPartition).
		Do(ctx).
		Into(result)
	return
}

// UpdateStatus was generated because the type contains a Status member.
// Add a +genclient:noStatus comment above the type to avoid generating UpdateStatus().
func (c *raftPartitions) UpdateStatus(ctx context.Context, raftPartition *v1beta2.RaftPartition, opts v1.UpdateOptions) (result *v1beta2.RaftPartition, err error) {
	result = &v1beta2.RaftPartition{}
	err = c.client.Put().
		Namespace(c.ns).
		Resource("raftpartitions").
		Name(raftPartition.Name).
		SubResource("status").
		VersionedParams(&opts, scheme.ParameterCodec).
		Body(raftPartition).
		Do(ctx).
		Into(result)
	return
}

// Delete takes name of the raftPartition and deletes it. Returns an error if one occurs.
func (c *raftPartitions) Delete(ctx context.Context, name string, opts v1.DeleteOptions) error {
	return c.client.Delete().
		Namespace(c.ns).
		Resource("raftpartitions").
		Name(name).
		Body(&opts).
		Do(ctx).
		Error()
}

// DeleteCollection deletes a collection of objects.
func (c *raftPartitions) DeleteCollection(ctx context.Context, opts v1.DeleteOptions, listOpts v1.ListOptions) error {
	var timeout time.Duration
	if listOpts.TimeoutSeconds != nil {
		timeout = time.Duration(*listOpts.TimeoutSeconds) * time.Second
	}
	return c.client.Delete().
		Namespace(c.ns).
		Resource("raftpartitions").
		VersionedParams(&listOpts, scheme.ParameterCodec).
		Timeout(timeout).
		Body(&opts).
		Do(ctx).
		Error()
}

// Patch applies the patch and returns the patched raftPartition.
func (c *raftPartitions) Patch(ctx context.Context, name string, pt types.PatchType, data []byte, opts v1.PatchOptions, subresources ...string) (result *v1beta2.RaftPartition, err error) {
	result = &v1beta2.RaftPartition{}
	err = c.client.Patch(pt).
		Namespace(c.ns).
		Resource("raftpartitions").
		Name(name).
		SubResource(subresources...).
		VersionedParams(&opts, scheme.ParameterCodec).
		Body(data).
		Do(ctx).
		Into(result)
	return
}

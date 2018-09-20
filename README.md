# Android MVC / MVP / MVVM
For Android MVC / MVP / MVVM patterns

### 简单示例演示如何从自由写法改进到MVC再改进到MVP - NoMVX

----

本来不懂标题说的这些东西（以及MVVM），看了这些文章之后

https://www.techyourchance.com/mvp-mvc-android-1/
https://academy.realm.io/posts/eric-maxwell-mvc-mvp-and-mvvm-on-android/
https://github.com/florina-muntenescu/MVPvsMVVM
http://tangpj.com/2016/12/04/mvvm-structure-introduction/#comments

可能还有一些其他资料没能一一列出，然后感觉大部分说的写的都是很深奥很神秘。然后我自己有点想法，不过：
`以下内容纯属个人观点，不喜请喷，谢谢各位大神，走过路过加个评论`

先看下这个示例是啥样的。

![](https://upload-images.jianshu.io/upload_images/5095851-b4bf546f83922b4f.gif?imageMogr2/auto-orient/strip)


然后我再说下这个示例都包括了什么。
主页面就是Android Studio里面新建Project的时候选定了带Navigation Drawer的那个Activity，然后我在里面包含了5个Fragment。
1. MainFragment - 就是看到的那个Hello MVX
2. NoMVXFragment - 不使用任何的MVX模式，`所有东西`直接在Fragment（或Activity，示例中是Fragment) 写代码
3. MVCFragment - 同样功能改成MVC模式看看怎么写
4. MVPFragment - 再改成MVP呢？看看啥区别
5. MVP2Fragment - 换一种MVP实现，对比下哪个好

`所有东西`就是我要的功能，这里的功能很简单，共有3个，纯是demo用：
1. User List - 用户列表展示的功能
2. Add User - 添加用户的功能
3. EmptyView - 初始状态下，ListView内容为空，内容显示的功能

从User List开始说吧。我直接从Activity进入Fragment，从Fragment开始说，回头再说Activity的事儿。

### NoMVXFragment -- 不使用任何的MVX模式
----
代码如下，关注点在下面：
```
public class NoMVXUserListFragment extends Fragment implements IMainPresenter.IFabListener {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private ListView mUserListView;

    private UserListAdapter mUserListAdapter;

    private List<UserModel> mUserModels;


    public NoMVXUserListFragment() {

    }

    public static NoMVXUserListFragment newInstance() {
        return new NoMVXUserListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        init(view);
        return view;
    }

    private void init(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mUserListView = rootView.findViewById(R.id.user_list_view);
        mUserListAdapter = new UserListAdapter(null);
        mUserListView.setAdapter(mUserListAdapter);

        mRefreshLayout.setScrollUpChild(mUserListView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList();
            }
        });

        setListEmptyView(rootView);
    }

    private void setListEmptyView(View rootView) {
        View emptyView = rootView.findViewById(R.id.empty_list_layout);
        TextView emptyTextView = emptyView.findViewById(R.id.empty_list_text_view);
        String emptyText = getResources().getString(R.string.empty_text, "No MVX");
        emptyTextView.setText(emptyText);
        mUserListView.setEmptyView(emptyView);
    }

    private void loadList() {
        showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mUserModels = UserManager.callAPIToGetUserList();
                updateUI(mUserModels);
            }
        });
    }

    private void updateUI(final List<UserModel> userModels) {
        MainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                hideLoading();
                updateListView(userModels);
            }
        });
    }

    public void updateListView(List<UserModel> userModels) {
        mUserListAdapter.setUserModelList(userModels);
        mUserListAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Update UI from Fragment: " + userModels.size(), Toast.LENGTH_LONG).show();
    }

    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onFabClicked() {
        addUser();
    }

    private void addUser() {
        showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                UserModel userModel = UserManager.addUser();
                mUserModels.add(userModel);
                updateUI(mUserModels);
            }
        });
    }
}
```

关注点：
1. View从哪初始化的？
2. ListView的下拉监听在哪做的？
3. ListView的下拉回调是在哪里执行的？
4. ListView下拉回调（模拟）调用API数据加载完毕之后，更新UI是在哪里执行的？
5. Add User是怎么执行的？
6. ListView为空时EmptyView是怎么初始化的？

挨个看看啊：
1. View从哪初始化的？- 是直接在onCreateView里面初始化了所有引用到的view，换句话说就是Fragment知道所有View的存在。相关代码：
```
public View onCreateView(..) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        init(view);
        return view;
}

   private void init(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        ...

        setListEmptyView(rootView);
    }
```

2. ListView的下拉监听在哪做的？ - onCreateView > init方法中
```
mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList(); // 下拉回调
            }
        });
```

3. ListView的下拉回调是在哪里执行的？- onCreateView > init方法中直接调用了 loadList，而这个方法就是实际开发中调用API或查询数据库的地方，此时也写在了Fragment中
```
    private void loadList() {
        showLoading();
        BackgroundThreadPoster.post(new Runnable() { // 不用关注 BackgroundThreadPoster, 就是在子线程中执行这段代码
            @Override
            public void run() {
                mUserModels = UserManager.callAPIToGetUserList();
                updateUI(mUserModels);
            }
        });
    }
```

4. ListView下拉回调（模拟）调用API数据加载完毕之后，更新UI是在哪里执行的？- *updateUI()* 方法中  
```
    private void updateUI(final List<UserModel> userModels) {
        MainThreadPoster.post(new Runnable() {  // 不用关注 MainThreadPoster, 就是在主线程中执行这段代码
            @Override
            public void run() {
                hideLoading();
                updateListView(userModels);
            }
        });
    }
```

5. Add User是怎么执行的？- Fragment实现了一个接口方法，这个方法是会被Activity调用的，重点在这个接口的相关逻辑写在了哪里，此处呢也是直接写在了Fragment里面：
```
    @Override
    public void onFabClicked() {
        addUser();
    }

    private void addUser() {
        showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                UserModel userModel = UserManager.addUser();
                mUserModels.add(userModel);
                updateUI(mUserModels);
            }
        });
    }
```

6. ListView为空时EmptyView是怎么初始化的？- 直接在Fragment里面，至于EmptyView(此处是一个TextView) 他的内容是什么，就是Fragment自己处理的，无论是调用API还是直接从资源文件取字符串，他都负责了。也就是说Fragment必须参与初始化字符串的过程
```
    private void setListEmptyView(View rootView) {
        ...
        String emptyText = getResources().getString(R.string.empty_text, "No MVX");
        emptyTextView.setText(emptyText);
        mUserListView.setEmptyView(emptyView);
    }
```

可见从初始化View，给View加监听，执行API，更新UI，这些功能统统都写在了Fragment中。当这个Fragment功能更多的时候，比如加上ListView点击事件，搜索，过滤，滑动监听等功能的时候，这个Fragment的类会变得越来越大。

那么如果我们不想让他变大，试试 [MVC](https://www.jianshu.com/p/aabe77839655)，看看他能不能改善。

-------

## MVC
-----
### 简单示例演示如何从自由写法改进到MVC再改进到MVP - 2 - MVC

----

上一篇：  https://www.jianshu.com/p/1c66557fcc05 - 1 - NoMVX，没有任何模式的写法

----

改成MVC是啥样？

上一篇中页面和点击事件都写在同一个类中了，所以基本没什么层次可言。 要是说那个UserModel是分出去的，也行，毕竟如果把那个也写在Fragment里。。也可以。。只是让那个Fragment更大更乱一些。

那先看看MVC模式的图吧

![mvc.png](https://upload-images.jianshu.io/upload_images/5095851-e69e7007d337387b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我并不太愿意去解释这个图，因为这样会让这个文章看起来跟其他的一样 也会很大，我稍微说下自己的看法吧
1. View -- 他是啥？你可能会说他是Fragment，此时，可以这么理解，但我先说一句，Fragment更像是一个View的容器, 暂且到此为止吧，后面的几篇文章会解释
2. Controller -- 他是啥？简单说他处理用户在页面交互时的事件比如下拉，点击等
3. Model -- 这个简单就是UserModel了，里面有picId, name, desc这样的信息
4. 这里面最重要的是Controller层，他做整体的统筹调度


对着代码看吧，我尽量把代码写的简单易懂。
Model::
```
public class UserModel {

    private int picId;
    private String userName;
    private String userDesc;

    public UserModel(int picId, String userName, String userDesc) {
        this.picId = picId;
        this.userName = userName;
        this.userDesc = userDesc;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }
}
```

View::
```
public class MVCUserListFragment extends Fragment implements IMainPresenter.IFabListener {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private ListView mUserListView;

    private UserListAdapter mUserListAdapter;

    private UserListController mUserListController;

    public MVCUserListFragment() {
        // Required empty public constructor
    }

    public static MVCUserListFragment newInstance() {
        return new MVCUserListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mUserListView = rootView.findViewById(R.id.user_list_view);
        mUserListAdapter = new UserListAdapter(null);
        mUserListView.setAdapter(mUserListAdapter);
        mUserListController = new UserListController(this);

        mRefreshLayout.setScrollUpChild(mUserListView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUserListController.loadList();
            }
        });

        View emptyView = rootView.findViewById(R.id.empty_list_layout);
        TextView emptyTextView = emptyView.findViewById(R.id.empty_list_text_view);
        String emptyListViewTextString = mUserListController.getEmptyListText();
        String emptyText = getResources().getString(R.string.empty_text, emptyListViewTextString);
        emptyTextView.setText(emptyText);
        mUserListView.setEmptyView(emptyView);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void updateListView(List<UserModel> userModels) {
        mUserListAdapter.setUserModelList(userModels);
        mUserListAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Update UI from MVC " + userModels.size(), Toast.LENGTH_LONG).show();
    }

    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFabClicked() {
        mUserListController.addUser();
    }
}

```

Controller::

```
public class UserListController {

    private MVCUserListFragment mMVCUserListFragment;

    private List<UserModel> mUserModels;

    public UserListController(MVCUserListFragment MVCUserListFragment) {
        mMVCUserListFragment = MVCUserListFragment;
    }

    public String getEmptyListText() {
        return "MVC模式，默认没有数据，尝试下拉刷新吧！";
    }

    public void loadList() {
        mMVCUserListFragment.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mUserModels = UserManager.callAPIToGetUserList();
                updateUI(mUserModels);
            }
        });
    }

    public void addUser() {
        mMVCUserListFragment.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                UserModel userModel = UserManager.addUser();
                mUserModels.add(userModel);
                updateUI(mUserModels);
            }
        });
    }



    private void updateUI(final List<UserModel> userModels) {
        MainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mMVCUserListFragment.hideLoading();
                mMVCUserListFragment.updateListView(userModels);
            }
        });
    }
}

```

跟上一篇一样，关注点如下：

1. View从哪初始化的？
2. ListView的下拉监听在哪做的？
3. ListView的下拉回调是在哪里执行的？
4. ListView下拉回调（模拟）调用API数据加载完毕之后，更新UI是在哪里执行的？
5. Add User是怎么执行的？
6. ListView为空时EmptyView是怎么初始化的？

看下改成MVC的逻辑：
1. View是从Fragment初始化的 - 跟NoMVX一样
2. ListView的下拉监听也是在Fragment做的 - 跟NoMVX一样
3. ListView的下拉回调是在哪里执行的 - 这个是在Fragment里面执行的，但他跟NoMVX有本质的区别，那就是这里的回调是调用了Controller的方法，而不是调用Fragment类里面的方法
```
    private void init(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        ...
        mRefreshLayout.setScrollUpChild(mUserListView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUserListController.loadList(); // 重要：Fragment (View) 持有Controller的引用
            }
        });
       ...
    }
```

4. ListView下拉回调（模拟）调用API数据加载完毕之后，更新UI是在哪里执行的 - 这个是在Controller里面执行的：
```
    public void loadList() {
        mMVCUserListFragment.showLoading(); // 重要：Controller持有Fragment (View) 的引用
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mUserModels = UserManager.callAPIToGetUserList();
                updateUI(mUserModels);
            }
        });
    }
```

5. Add User是怎么执行的 - 这个跟第4点类似，Fragment（View）调用Controller, Controller调用API，完成后再调用Fragment (View) 的方法来更新UI -- 这个是MVC的主要执行过程
6. ListView为空时EmptyView是怎么初始化的 - 在Fragment初始化过程中调用了Controller的方法来得到要显示的内容：
```
    private void init(View rootView) {
        ...
        TextView emptyTextView = emptyView.findViewById(R.id.empty_list_text_view);
        String emptyListViewTextString = mUserListController.getEmptyListText(); // 此处调用了Controller的方法
        String emptyText = getResources().getString(R.string.empty_text, emptyListViewTextString);
        emptyTextView.setText(emptyText);
        mUserListView.setEmptyView(emptyView);
    }
```


----

我们来分析下把 Fragment 拆成 Fragment + Controller 有什么好处， 难道我们就是为了耍下花样吗？
1. Fragment现在类变小了。他基本上就是从layout里面加载一个布局，然后加上监听，不过监听回调都委派给了Controller，他也不用关心什么时候要更新UI了，这些都放到Controller中来处理了
2. Controller他的职责也很明确，那就是处理Fragment拆分出来的事情，主要可以概括为两点：
> 1. 调用API或数据库执行数据交互，并在完毕之后更新UI
> 2. Fragment中有一些不确定的因素，比如上例中，TextView显示的文字，可能也是需要根据运行时情况不同而返回不同的文字（比如，返回当前登录的用户名，当前页面名称等等），这样一来，Fragment是不需要关心这些与数据相关的细节的，都由Controller来负责了


你可能还会再问，现在Fragment是小了可他把所有的工作都交给Controller去做了，这就好像是把AB分成了A+B，有什么好处，你告诉我。

我得说如果你这么想了 那这是一个好问题
这么说吧，如果需求改变了
* 比如改变了UI，对于之前NoMVX来说， 是需要修改Fragment类的，而这个有可能会影响到数据访问方面的功能，当然你可以很小心的去做来保证他不受影响，但这个可能性还是存在且有点大的。如果对于MVC来说，你改Fragment不影响Controller，只要数据访问那边的需求不变，Controller几乎是不用动的
* 再比如改变了数据来源，原来是从数据库中取，现在改变成从服务器取，那NoMVX也是需要改Fragment的，而这个可能影响UI部分。对于MVC来说，只需要在Controller里面改就行了，而这个不影响Fragment的UI显示。

可以看出来MVC的职责更纯粹一些，Fragment在MVC模式中只负责View, Controller只负责数据交互然后更新UI。
看起来不错对吧？可为什么还要提到MVP呢？一定是MVC还有什么不足之处。
是的。
这里我只提一点，那就是Controller引用了Fragment的实例，而这个对单元测试是比较困难的，因为在Android环境之外的单元测试环境中难以模拟Fragment这个类。

（其他MVC的不足还请自行查看其他长篇的文字。。）

那MVP比MVC又好到哪了呢？

------------

## MVP
------

### 简单示例演示如何从自由写法改进到MVC再改进到MVP - 3 - MVP

----

上一篇：  https://www.jianshu.com/p/aabe77839655 - 2 - MVC，MVC的写法

----

改成MVP是啥样？

先看一个图吧。
![MVP.png](https://upload-images.jianshu.io/upload_images/5095851-b7948381344211a5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

从这个图中我们可以看出来，
1. View 和 Presenter各有一个接口， IView 和  IPresenter
2. View持有Presenter的引用，同时Presenter也持有View的引用

在上代码之前先说下，这个MVP的设计跟MVC类似，当用户操作View的时候，在View里面调用IPresenter的方法（此处为Presenter接口），而Presenter的实现类去处理数据相关的操作并更新UI。

其实这个地方，我感觉跟MVC非常相似，唯一的区别在于，MVP引入了接口，而MVC里面直接引用的是相关的类。

引入接口的好处就是，Presenter类里面不直接引用Fragment（或者其他的View \ Activity等Android里面特有的类），而是引用了IView, 这样以来就很容易在单元测试的时候写任何一个类来实现IView接口，这样使得单元测试更加容易进行。

上代码：

IView::
```
public interface IUserListView {

    void updateList(List<UserModel> userModels);

    void showLoading();

    void hideLoading();
}
```

View::
```
public class MVPUserListFragment extends Fragment implements IUserListView, IMainPresenter.IFabListener {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private IUserListPresenter mListPresenter;

    private ListView mUserListView;

    private UserListAdapter mUserListAdapter;

    public MVPUserListFragment() {
        super();
    }

    public static MVPUserListFragment newInstance() {
        return new MVPUserListFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = getFragmentView(inflater, container);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        mListPresenter = new UserUserListPresenter(this);
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mUserListView = rootView.findViewById(R.id.user_list_view);
        mUserListAdapter = new UserListAdapter(null);
        mUserListView.setAdapter(mUserListAdapter);

        mRefreshLayout.setScrollUpChild(mUserListView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListPresenter.loadList();
            }
        });

        View emptyView = rootView.findViewById(R.id.empty_list_layout);
        TextView emptyTextView = emptyView.findViewById(R.id.empty_list_text_view);
        String emptyListViewTextString = mListPresenter.getEmptyListText();
        String emptyText = getResources().getString(R.string.empty_text, emptyListViewTextString);
        emptyTextView.setText(emptyText);
        mUserListView.setEmptyView(emptyView);
    }

    private View getFragmentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void updateList(List<UserModel> userModels) {
        hideLoading();
        mUserListAdapter.setUserModelList(userModels);
        mUserListAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Update UI from MVP", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFabClicked() {
        mListPresenter.addUser();
    }
}
```

IPresenter:
```
public interface IUserListPresenter {
    String getEmptyListText();
    void loadList();
    void addUser();
}
```

Presenter:
```
public class UserUserListPresenter implements IUserListPresenter {

    private IUserListView mListView;

    private List<UserModel> mUserModels;

    public UserUserListPresenter(IUserListView iUserListView) {
        mListView = iUserListView;
    }

    @Override
    public String getEmptyListText() {
        return "MVP - NO DATA FOR NOW! ";
    }

    @Override
    public void loadList() {
        mListView.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mUserModels = UserManager.callAPIToGetUserList();
                updateUI(mUserModels);
            }
        });
    }

    @Override
    public void addUser() {
        mListView.showLoading();
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                UserModel userModel = UserManager.addUser();
                mUserModels.add(userModel);
                updateUI(mUserModels);
            }
        });
    }

    private void updateUI(final List<UserModel> userModels) {
        MainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mListView.updateList(userModels);
            }
        });
    }
}
```

这个MVP的另一个好处是，通过定义IView和IPresenter接口，很容易知道实现类有什么功能。

但这个MVP实现中，还是直接用Fragment实现了IView接口，而我比较同意这个观点:
https://www.techyourchance.com/activities-android/
也就是说，Activity/Fragment 更应该被当做Controller / Presenter的角色来看待。

这样就有了[MVP2](https://www.jianshu.com/p/8fb3f5a07dac)的做法。

-----------------

## MVP2
------

### 简单示例演示如何从自由写法改进到MVC再改进到MVP - 4 - MVP2

----

上一篇：  https://www.jianshu.com/p/aabe77839655 - 3 - MVP，MVP的写法

----

另一种MVP2是啥样？
这个模式我还真没找到相关的图，那就描述一下吧！

上一篇中MVP的设计图是这样的：
![MVP.png](https://upload-images.jianshu.io/upload_images/5095851-b7948381344211a5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这个MVP2跟他类似，但有两个区别：
一个区别是在于上一篇中提到的：
> 这（上）个MVP实现中，还是直接用Fragment实现了IView接口


另外一个区别是：
> View不直接引用Presenter，而是通过接口


这个设计中Fragment作为中介，实现了IView和IPresenter中的回调接口。

先看Fragment和IView的分离：
简单的说就是在Fragment的`onCreateView`方法中返回一个View，而这个View实现了IView接口。

我们先看下IView::

```
public interface IBaseView {

    View getRootView();

    Context getContext();
}
```

`IBaseView`是一个基础接口，具体的页面应该有自己的一个IView，比如：
```
public interface IUserListView extends IBaseView {

    interface IListViewHandler {
        void onRefreshList(); // 重点：注意这个是连接View 和 Presenter的接口方法
    }

    void updateList(List<UserModel> userModels);

    void showLoading();

    void hideLoading();

    void setViewHandler(IListViewHandler handler);

    void setEmptyListText(String emptyListViewTextString);
}
```

而这个`IUserListView`的实现类是这样的：
```
public class UserListView implements IUserListView {

    private ScrollChildSwipeRefreshLayout mRefreshLayout;

    private ListView mUserListView;

    private UserListAdapter mUserListAdapter;

    private IListViewHandler mListViewHandler; // 重点：没有引用 Presenter，而是实现了一个回调接口

    private View mEmptyView;

    private TextView mEmptyTextView;

    private View mRootView;

    private Context mContext;

    public UserListView(LayoutInflater layoutInflater, ViewGroup container) {
        mRootView = layoutInflater.inflate(getLayoutId(), container, false);
        mContext = mRootView.getContext();
        init(mRootView);
    }

    private int getLayoutId() {
        return R.layout.fragment_list;
    }

    protected void init(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mUserListView = rootView.findViewById(R.id.user_list_view);
        mUserListAdapter = new UserListAdapter(null);
        mUserListView.setAdapter(mUserListAdapter);

        mRefreshLayout.setScrollUpChild(mUserListView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mListViewHandler != null) {
                    mListViewHandler.onRefreshList();
                }
            }
        });

        mEmptyView = rootView.findViewById(R.id.empty_list_layout);
        mEmptyTextView = mEmptyView.findViewById(R.id.empty_list_text_view);
    }


    @Override
    public void updateList(List<UserModel> userModels) {
        hideLoading();
        mUserListAdapter.setUserModelList(userModels);
        mUserListAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Update UI", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setViewHandler(IListViewHandler handler) {
        mListViewHandler = handler;
    }

    @Override
    public void setEmptyListText(String emptyListViewTextString) {
        String emptyText = mContext.getResources().getString(R.string.empty_text, emptyListViewTextString);
        mEmptyTextView.setText(emptyText);
        mUserListView.setEmptyView(mEmptyView);
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Context getContext() {
        if (mRootView == null) {
            return null;
        }
        return mRootView.getContext();
    }
}
```

再看Fragment的实现，一切都就清楚了。

1. Fragment中实现了`IListViewHandler` 和 `ListUpdaterListener`, 他们分别是IView 和 IPresenter 的回调。
2. 当用户操作（比如下拉）时调用`IListViewHandler`的回调方法，此时执行到`Fragment`里面的代码，
3. 在`Fragment`里面调用`Presenter`的方法执行数据交互
4. 然后`Presenter`调用`ListUpdaterListener`的方法触发到`Fragment`里面的回调
5. `Fragment`再调用`IView`的方法来完成UI更新

这几步完成了IView和IPresenter的解耦。

```
public class MVP2UserListFragment extends Fragment implements IUserListView.IListViewHandler,
        UserListPresenter.ListUpdaterListener, IMainPresenter.IFabListener {

    private IUserListView mListView;

    IUserListPresenter mPresenter;

    public MVP2UserListFragment() {
        super();
    }

    public static MVP2UserListFragment newInstance() {
        return new MVP2UserListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UserListPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListView = new UserListView(inflater, container);
        mListView.setViewHandler(this);
        mListView.setEmptyListText(mPresenter.getEmptyListText());
        return mListView.getRootView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.registerListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unregisterListener(this);
    }

    @Override
    public void onRefreshList() {
        mListView.showLoading();
        mPresenter.loadList();
    }

    @Override
    public void updateList(List<UserModel> userModels) {
        mListView.hideLoading();
        mListView.updateList(userModels);
    }

    @Override
    public void onFabClicked() {
        mListView.showLoading();
        mPresenter.addUser();
    }
}
```

贴下`IPresenter`:

```
public interface IUserListPresenter<T> extends IBasePresenter<T> {

    void loadList();

    void addUser();

    String getEmptyListText();
}
```

和`Presenter`
```
public class UserListPresenter extends BasePresenter<UserListPresenter.ListUpdaterListener> implements IUserListPresenter<UserListPresenter.ListUpdaterListener> {

    private List<UserModel> mUserModels;

    public UserListPresenter() {
    }

    public void loadList() {
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                mUserModels = UserManager.callAPIToGetUserList();
                updateUI(mUserModels);
            }
        });
    }

    public void addUser() {
        BackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                UserModel userModel = UserManager.addUser();
                mUserModels.add(userModel);
                updateUI(mUserModels);
            }
        });
    }

    @Override
    public String getEmptyListText() {
        return "MVP2 :: ";
    }

    private void updateUI(final List<UserModel> userModels) {
        MainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                for (ListUpdaterListener listener : getListeners()) {
                    listener.updateList(userModels);
                }
            }
        });
    }

    public interface ListUpdaterListener {
        void updateList(List<UserModel> userModels);
    }
}
```

package com.example.win.easy.web;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.web.dto.SongDTO;
import com.example.win.easy.web.dto.SongListDTO;

/**
 * <p>从DTO到DO转化的工具（当然以后也有可能实现DO到DTO的转化）</p>
 * <p>之所以把转化单独摘出来一个类，主要是因为如果这些个方法写在不同类的内部就很难用，为啥呢？</p>
 * <p></p>
 * <p>首先如果写在其他类里面，那应该private还是public呢？如果private，那么很难mock（或者说mock起来很丑而且还要用PowerMock的修改字节码的方式）</p>
 * <p>不但如此，其他类万一需要用，那就得重写，代码就开始复制粘贴了。那行，设置成public成了吧？也不成。为啥？</p>
 * <p>看起来public是一举解决了上面两个问题，但现实是，需要这些转化方法的类大多都是某些中间件（比如{@link com.example.win.easy.web.service.SongListWebService}）</p>
 * <p>如果其他中间件仅仅是为了调用一个转化方法就把这个类加入自己的依赖类中，也太傻X了。。按理来说应该是依赖其核心功能的时候才应该添加依赖啊，可只是为了转化这个。。</p>
 * <p>所以从职责分配的角度，转化这个方法应当单独提出来作为一个类</p>
 * <p></p>
 * <p>再有就是这些转化方法（就是这一堆的toDO）为什么没有做成静态方法（因为可以静态import就可以少写一个"."的调用）？</p>
 * <p>其实这个主要是因为依赖和测试。如果做成静态方法，那么万一这个Util类以后需要依赖其他类，那么就很难写，因为方法都是静态的，</p>
 * <p>万一（只是说万一）你需要的某个类还没被加载，那就很难办；或者说即便你可以隐式地在某处设置一下，也是奇丑无比，说不定什么时候就忘了有这个初始化</p>
 * <p>也就是说，从依赖的角度，静态方法会导致静态依赖，而静态依赖的初始化往往不能写在构造函数里面，这就导致你很难一眼看出来这个类都依赖了那些类，万一出了bug就不太好排查</p>
 * <p>同时，何时注入依赖也很难搞，static代码估计是不行，因为大概率你依赖的类在那时候还没有加载或者没有初始化，</p>
 * <p>除非你专门搞一个类，在确保所有类都加载完了之后给它们初始化（事实上在一开始没有用Dagger解决依赖注入之前，这个项目确实用了一个ClassLoader类来解决静态依赖。。）</p>
 * <p>但是不用我说也能看出来，这也，太丑了，而且要手动管理初始化顺序，说不定哪天代码一改就凉了</p>
 * <p>上面是从依赖的角度分析，实际上从测试的角度分析，做成静态方法要测试也很麻烦。</p>
 * <p>试想如果一个类A用到了这个Util，那么你在测试A类的时候，就要mock掉这个转化的方法，假如是静态方法，你就必须很hack地用PowerMock提供的</p>
 * <p>奇技淫巧才能mock静态方法，想想就头大。实际上我个人认为mock静态方法应当是迫不得已时才启用的方法（比如要mock的类是库类，没法修改）</p>
 * <p>综上所述，把这个类提取了出来，方法也没有做成静态。</p>
 */
public class DTOUtil {

    public SongListDO toDO(SongListDTO songListDTO){
        //TODO SongListDTO到SongListDO的转化
        return null;
    }

    public SongDO toDO(SongDTO songDTO){
        //TODO SongDTO到SongDO的转化
        return null;
    }

}
